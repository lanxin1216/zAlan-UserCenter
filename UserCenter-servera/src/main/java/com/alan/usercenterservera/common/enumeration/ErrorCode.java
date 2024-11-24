package com.alan.usercenterservera.common.enumeration;

/**
 * 自定义错误码
 *
 * @author alan
 */
public enum ErrorCode {

    SUCCESS(0, "success", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求参数为空", ""),
    LOGIN_ERROR(40002, "登陆失败", ""),
    REGISTER_ERROR(40003, "注册失败", ""),
    NO_AUTH(40100, "无权限", ""),
    NOT_LOGIN(40101, "未登录", ""),
    NOT_FOUND(40400, "请求数据不存在", ""),
    SYSTEM_ERROR(50000, "系统内部错误", "");


    /**
     * 状态码
     */
    private final int code;

    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码描述（详情）
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
