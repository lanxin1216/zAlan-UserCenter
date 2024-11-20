package com.alan.usercenterservera.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author alan
 * 用户注册请求体
 */
@Data
public class UserRegisterRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -1L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 校验密码
     */
    private String checkPassword;
}
