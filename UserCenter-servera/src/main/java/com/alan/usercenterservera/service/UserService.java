package com.alan.usercenterservera.service;

import com.alan.usercenterservera.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author alan
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-11-18 21:56:54
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */
    long userRegister (String userAccount, String userPassword, String checkPassword);

}
