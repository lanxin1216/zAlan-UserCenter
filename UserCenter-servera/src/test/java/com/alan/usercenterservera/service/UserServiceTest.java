package com.alan.usercenterservera.service;

import com.alan.usercenterservera.model.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author alan
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    /**
     * 添加用户测试
     * -测试 Mybatis-plus 使用
     */
    @Test
    void testAddUser() {
        User user = new User();
        user.setUsername("lanxin");
        user.setUserAccount("alan");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setUserPassword("123456789");
        user.setPhone("11112555896");
        user.setEmail("11111@qq.com");
        user.setUserStatus(0);

        boolean result = userService.save(user);
        Assertions.assertTrue(result); // 添加断言 result 为true
    }

    /**
     * 注册测试
     */
    @Test
    void userRegister() {
        String userAccount = "zsy";
        String userPassword;
        String checkPassword = "123456789";

        /* 测试密码为空 */
        userPassword = "";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result); // 添加断言，密码为空，期望值应该为-1

        /* 测试账号小于4位 */
        userAccount = "al";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result); // 添加断言，账号小于4位，期望值应该为-1

        /* 测试密码小于8位 */
        userPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result); // 添加断言，密码小于8位，期望值应该为-1

        /* 测试两次密码不一致 */
        userPassword = "12345678910";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result); // 添加断言，两次密码不一致，期望值应该为-1

        /* 测试账号包含特殊字符 */
        userAccount = "al&n";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);
        userAccount = "al{]n";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        /* 测试账号重复 */
        userAccount = "alan";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        /* 正常注册账号 */
        userAccount = "zhangsiying";
        userPassword = "123456789";
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertTrue(result > 0); // 添加断言，正常注册账号，期望值返回的id大于0
    }
}