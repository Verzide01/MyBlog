package com.piggod.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.piggod.common.domain.po.LoginUser;
import com.piggod.common.domain.po.User;
import com.piggod.common.domain.vo.BlogUserLoginVO;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.domain.vo.UserInfoVO;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.exception.SystemException;
import com.piggod.common.mapper.UserMapper;
import com.piggod.common.service.IBlogLoginService;
import com.piggod.common.utils.JwtUtil;
import com.piggod.common.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static com.piggod.common.constants.SystemConstants.BLOG_LOGIN_KEY_PREFIX;

@Service
public class BlogLoginServiceImpl implements IBlogLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        if (Objects.isNull(user)) {
            throw new RuntimeException("请输入账号和密码");
        }

        if (!StringUtils.hasText(user.getUserName()) || !StringUtils.hasText(user.getPassword())) {
            // 用户名或密码为空
            throw new SystemException(
                    StringUtils.hasText(user.getUserName()) ?
                            AppHttpCodeEnum.REQUIRE_PASSWORD : AppHttpCodeEnum.REQUIRE_USERNAME
            );

        }


        // 1.调用AuthenticationManager实现类的authenticate方法进行登录认证
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 2.校验数据
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        if (Objects.isNull(loginUser)) {
            throw new RuntimeException("账号或密码错误");
        }

        // 3.通过则生成token并存入redis 并且封装返回用户信息
        Long userId = loginUser.getUser().getId();
        String jwt = JwtUtil.createJWT(userId.toString());

        // 用户信息存入redis
        redisCache.setCacheObject(BLOG_LOGIN_KEY_PREFIX+ userId, loginUser);

        UserInfoVO userInfoVO = BeanUtil.toBean(loginUser.getUser(), UserInfoVO.class);
        BlogUserLoginVO blogUserLoginVO = new BlogUserLoginVO(jwt, userInfoVO);


        return ResponseResult.okResult(blogUserLoginVO);
    }

    @Override
    public ResponseResult logout() {
        // 1.解析token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        if (Objects.isNull(loginUser)) {
            return ResponseResult.okResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        // 2.封装redis key
        Long userId = loginUser.getUser().getId();

        // 3.删除用户信息
        redisCache.deleteObject(BLOG_LOGIN_KEY_PREFIX + userId);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
