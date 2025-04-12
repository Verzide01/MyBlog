package com.piggod.common.aop;

import com.alibaba.fastjson.JSON;
import com.piggod.common.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect // 表明是一个切面 @pointcut注解表明是切点
@Component
@Slf4j
public class LogAspect {

    // 定义切点路径
    @Pointcut("@annotation(com.piggod.common.annotation.SystemLog)")
    public void pointCut() {
    }

    // 环绕通知
    @Around("pointCut()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;

        try {
            // 1.在接口执行前进行打印执行前的数据
            handleBefore(joinPoint);

            // proceed方法相当于让目标接口执行
            result = joinPoint.proceed();

            // 2.在接口执行后进行打印执行后数据

            handleAfter(JSON.toJSONString(result));
        } finally {
            // 结束后换行
            log.info("=======================End=======================" + System.lineSeparator());
        }


        return result;
    }


    private void handleBefore(ProceedingJoinPoint joinPoint) {
        // 1.获取url
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        StringBuffer requestURL = request.getRequestURL();

        // 2.获取业务描述
        MethodSignature methodSignature = getUseAnnotationClass(joinPoint);
        SystemLog systemLog = methodSignature.getMethod().getAnnotation(SystemLog.class);
        String bussinessName = systemLog.bussinessName();

        // 3.请求方式
        String method = request.getMethod();

        // 4.请求类名
        String declaringTypeName = methodSignature.getDeclaringTypeName();
        String name1 = methodSignature.getName();


        // 5.访问IP
        String remoteHost = request.getRemoteHost();

        // 6.传入参数
        String args = JSON.toJSONString(joinPoint.getArgs());


        log.info("======================Start======================");
        // 打印请求 URL
        log.info("请求URL   : {}", requestURL);
        // 打印描述信息
        log.info("接口业务描述: {}", bussinessName);
        // 打印 Http method
        log.info("请求方式   : {}", method);
        // 打印调用 controller 的全路径以及执行方法
        log.info("请求类名   : {}.{}", declaringTypeName, name1);
        // 打印请求的 IP
        log.info("访问IP    : {}", remoteHost);
        // 打印请求入参
        log.info("传入参数   : {}", args);
        // 打印出参
    }

    /**
     * 获取使用了注解的类
     *
     * @param joinPoint
     */
    private MethodSignature getUseAnnotationClass(ProceedingJoinPoint joinPoint) {
        MethodSignature method = ((MethodSignature) joinPoint.getSignature());
        return method;

    }


    private void handleAfter(String result) {
        log.info("返回参数   : {}", result);

    }
}
