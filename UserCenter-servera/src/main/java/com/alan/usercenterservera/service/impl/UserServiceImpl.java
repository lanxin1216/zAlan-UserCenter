package com.alan.usercenterservera.service.impl;

import com.alan.usercenterservera.common.enumeration.ErrorCode;
import com.alan.usercenterservera.exception.BusinessException;
import com.alan.usercenterservera.mapper.UserMapper;
import com.alan.usercenterservera.model.domain.User;
import com.alan.usercenterservera.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.alan.usercenterservera.constant.UserConstant.USER_LOGIN_STATE;

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
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名过短");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码过短");
        }
        // 校验两次密码是否一致
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不一致");
        }
        // 校验账号不包含特殊字符
        String validPattern = "\\p{P}|\\p{S}|\\s+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号包含特殊字符");
        }

        // 校验账号是否重复
        QueryWrapper<User> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("user_account", userAccount);
        // 查询数据库
        Long count = userMapper.selectCount(objectQueryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
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
            throw new BusinessException(ErrorCode.REGISTER_ERROR, "注册失败,请稍后重试");
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
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名过短");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码过短");
        }
        // 校验账号不包含特殊字符
        String validPattern = "\\p{P}|\\p{S}|\\s+";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号包含特殊字符");
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
            throw new BusinessException(ErrorCode.LOGIN_ERROR, "登录失败，用户名或密码错误。");
        }

        /* 数据脱敏 */
        User safetyUser = getSafetyUser(user);
        /* 记录保存登录态 */
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    /**
     * 搜索获取用户列表（根据用户名模糊搜索）
     *
     * @param userName 用户名
     * @return 返回用户列表
     */
    @Override
    public List<User> searchUsers(String userName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 模糊搜索;首先判断uerName是否为空或''
        if (StringUtils.isNotBlank(userName)) {
            queryWrapper.like("username", userName);
        }
        List<User> userList = userMapper.selectList(queryWrapper);
        List<User> safetyUserList = new ArrayList<>();

        // List<User> safetyUserList = userList.stream().map(user -> getSafetyUser(user)).collect(Collectors.toList()); 使用stream流的方法

        for (User user : userList) {
            User safetyUser = getSafetyUser(user);
            safetyUserList.add(safetyUser);
        }
        return safetyUserList;
    }

    /**
     * 删除用户
     *
     * @param id 用户 id
     * @return 返回删除结果
     */
    @Override
    public boolean deleteUser(Long id) {
        if (id <= 0) {
            return false;
        }
        // 删除用户-返回修改的行数
        int result = userMapper.deleteById(id);
        return result != 0;
    }

    /**
     * 用户注销
     *
     * @param request 请求
     * @return 返回注销结果
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }


    /**
     * 用户数据脱敏
     */
    private User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setCreateTime(originUser.getCreateTime());

        return safetyUser;
    }

}

