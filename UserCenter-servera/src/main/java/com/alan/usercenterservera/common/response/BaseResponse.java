package com.alan.usercenterservera.common.response;

import com.alan.usercenterservera.common.enumeration.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @param <T>
 * @author alan
 */
@Data
public class BaseResponse<T> implements Serializable {

    private Integer code; // 状态码
    private String message; //错误信息
    private String description; //描述
    private T data; //数据

    public BaseResponse(Integer code, T data, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
        this.data = data;
    }

    public BaseResponse(Integer code, T data, String message) {
        this.code = code;
        this.message = message;
        this.description = "";
        this.data = data;
    }

    public BaseResponse(Integer code, T data) {
        this.code = code;
        this.message = "";
        this.description = "";
        this.data = data;
    }

    public BaseResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.description = errorCode.getDescription();
        this.data = null;
    }
}
