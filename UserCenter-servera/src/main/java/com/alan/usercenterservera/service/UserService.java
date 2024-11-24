package com.alan.usercenterservera.service;

import com.alan.usercenterservera.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author alan
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2024-11-18 21:56:54
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户名
     * @param userPassword 密码
     * @param request      请求
     * @return 返回用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 搜索获取用户列表（根据用户名模糊搜索）
     *
     * @param userName 用户名
     * @return 返回用户列表
     */
    List<User> searchUsers(String userName);

    /**
     * 删除用户
     *
     * @param id 用户 id
     * @return 返回删除结果
     */
    boolean deleteUser(Long id);

    /**
     * 用户注销
     *
     * @param request 请求
     * @return 返回注销结果
     */
    int userLogout(HttpServletRequest request);
}
