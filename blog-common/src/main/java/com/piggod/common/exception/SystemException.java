package com.piggod.common.exception;

import com.piggod.common.enums.AppHttpCodeEnum;
import lombok.Getter;

//统一异常处理
@Getter
public class SystemException extends RuntimeException{

    private int code;

    private String msg;



    public SystemException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        //把某个枚举类里面的code和msg赋值给异常对象
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }
}