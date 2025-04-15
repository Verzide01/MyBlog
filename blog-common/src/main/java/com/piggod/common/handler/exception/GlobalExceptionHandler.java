package com.piggod.common.handler.exception;

import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.UnexpectedTypeException;
import java.util.Objects;
import java.util.stream.Collectors;


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

        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "出现异常，请联系管理员！");
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

    //HttpRequestMethodNotSupportedException    例如接口为POT请求， 你发送的是GET请求 则会报错
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 设置 HTTP 状态码为 400（Bad Request）
    public ResponseResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error("错误的请求方式! {}", ex);
        // 自定义错误消息
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error("错误的请求参数! {}", ex);
        // 自定义错误消息
        return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseResult handleUnexpectedTypeException(UnexpectedTypeException ex) {
        log.error("错误的请求参数! {}", ex);
        // 自定义错误消息
        return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
    }








}
