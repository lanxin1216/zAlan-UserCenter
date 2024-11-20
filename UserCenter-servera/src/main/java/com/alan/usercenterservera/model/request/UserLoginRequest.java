package com.alan.usercenterservera.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author alan
 * 用户登录请求体
 */
@Data
public class UserLoginRequest implements Serializable {
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
}
