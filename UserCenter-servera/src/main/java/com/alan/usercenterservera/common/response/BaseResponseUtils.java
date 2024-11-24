package com.alan.usercenterservera.common.response;

import com.alan.usercenterservera.common.enumeration.ErrorCode;

/**
 * 通用返回工具类
 *
 * @author alan
 */
public class BaseResponseUtils {

    // 添加私有构造函数-不允许被实例化
    private BaseResponseUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }

    /**
     * 成功返回
     *
     * @param data 返回数据
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "success");
    }

    /**
     * 失败返回
     *
     * @param code        错误码
     * @param message     错误信息
     * @param description 错误描述
     */
    public static <T> BaseResponse<T> error(int code, String message, String description) {
        return new BaseResponse<>(code, null, message, description);
    }

    /**
     * 失败返回
     *
     * @param errorCode   错误码枚举
     * @param message     错误信息
     * @param description 错误描述
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse<>(errorCode.getCode(), null, message, description);
    }

    /**
     * 失败返回
     *
     * @param errorCode   错误码枚举
     * @param description 错误描述
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode, String description) {
        return new BaseResponse<>(errorCode.getCode(), null, errorCode.getMessage(), description);
    }

    /**
     * 失败返回
     *
     * @param errorCode 错误码枚举
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }
}
