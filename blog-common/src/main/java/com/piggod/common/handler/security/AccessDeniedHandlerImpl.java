package com.piggod.common.handler.security;

import com.alibaba.fastjson.JSON;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 处理security内部的异常
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        accessDeniedException.printStackTrace();
        ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        String json = JSON.toJSONString(result);
        // 响应给前端
        WebUtils.renderString(response, json);
    }
}
