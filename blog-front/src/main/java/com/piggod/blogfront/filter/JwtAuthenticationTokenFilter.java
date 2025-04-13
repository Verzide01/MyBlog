package com.piggod.blogfront.filter;

import com.alibaba.fastjson.JSON;
import com.piggod.common.domain.po.LoginUser;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.utils.JwtUtil;
import com.piggod.common.utils.RedisCache;
import com.piggod.common.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static com.piggod.common.constants.SystemConstants.BLOG_LOGIN_KEY_PREFIX;


@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1.获取jwt
        String token = request.getHeader("token");
        if (Objects.isNull(token)){
            filterChain.doFilter(request, response);
            return;
        }
        // 2.校验
        String userId = "";
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();

        } catch (Exception e) {
            // 3.不通过则放行进行下一个过滤器
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }

        // 4.从redis获取用户信息存入securityContextHolder中
        LoginUser loginUser = redisCache.getCacheObject(BLOG_LOGIN_KEY_PREFIX + userId);
        if (Objects.isNull(loginUser)){
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }

        // todo 设置权限信息
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 放行
        filterChain.doFilter(request, response);
    }
}
