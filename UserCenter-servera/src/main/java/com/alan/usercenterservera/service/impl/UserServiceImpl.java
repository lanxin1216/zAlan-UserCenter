package com.alan.usercenterservera.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.alan.usercenterservera.model.domain.User;
import com.alan.usercenterservera.service.UserService;
import com.alan.usercenterservera.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author alan
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-11-18 21:56:54
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Resource
    private UserMapper userMapper;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "alan";

    /**
     * 用户登录态
     */
    private static final String USER_LOGIN_STATE = "userLoginState";

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        /* 校验 */
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }
        if (userAccount.length() < 4) {
            return -1;
        }
        if (userPassword.length() < 8) {
            return -1;
        }
        // 校验两次密码是否一致
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        // 校验账号不包含特殊字符
        String validPattern = "\\p{P}|\\p{S}|\\s+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }

        // 校验账号是否重复
        QueryWrapper<User> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("user_account", userAccount);
        // 查询数据库
        Long count = userMapper.selectCount(objectQueryWrapper);
        if (count > 0) {
            return -1;
        }

        /* 密码加密 */
        // 使用MD5算法和盐值
        String encodedPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        /* 插入数据 */
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encodedPassword);
        // 将用户信息保存到数据库
        boolean saveResult = this.save(user);
        // 判断是否保存成功
        if (!saveResult) {
            return -1;
        }

        return user.getId();
    }


    /**
     * 用户登录
     *
     * @param userAccount  用户名
     * @param userPassword 密码
     * @param request      请求
     * @return 脱敏后的用户信息
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        /* 校验 */
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (userPassword.length() < 8) {
            return null;
        }
        // 校验账号不包含特殊字符
        String validPattern = "\\p{P}|\\p{S}|\\s+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }
        /* 加密 */
        String encodedPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_password", encodedPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            return null;
        }

        /* 数据脱敏 */
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());

        /* 记录保存登录态 */
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

}

