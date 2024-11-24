package com.alan.usercenterservera.exception.handler;

import com.alan.usercenterservera.common.enumeration.ErrorCode;
import com.alan.usercenterservera.common.response.BaseResponse;
import com.alan.usercenterservera.common.response.BaseResponseUtils;
import com.alan.usercenterservera.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author alan
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理-业务异常
     */
    @ExceptionHandler(value = BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return BaseResponseUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    /**
     * 处理-系统异常
     */
    @ExceptionHandler(value = RuntimeException.class)
    public BaseResponse<?> systemExceptionHandler(Exception e) {
        log.error("systemException: ", e);
        return BaseResponseUtils.error(ErrorCode.SYSTEM_ERROR, "");
    }

}
