package com.piggod.common.handler.exception;

import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;


// 处理springmvc内部的异常
// 全局异常处理 所有的异常都会在这里进行处理
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 拦截并处理SystemException的异常
    @ExceptionHandler(value = SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e) {
        // 打印错误信息
        log.error("出现了异常! {}", e);

        // 从异常对象中获取提示信息封装，然后返回
        return ResponseResult.errorResult(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseResult exceptionHandler(Exception e) {
        log.error("出现了异常! {}", e);

        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, e.getMessage());
    }

    // 拦截并处理BindException的异常 主要是使用了@valid注解的校验异常
    @ExceptionHandler(value = BindException.class)
    public ResponseResult bindExceptionHandler(BindException e) {
        log.error("出现了异常! {}", e);

        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
    }

    // 当post请求需要requestbody注解获取对象时， 传入了一个空的json数据就会导致异常
    // 捕获 HttpMessageNotReadableException 异常
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 设置 HTTP 状态码为 400（Bad Request）
    public ResponseResult handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("出现了异常! {}", ex);
        // 自定义错误消息
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }
}
