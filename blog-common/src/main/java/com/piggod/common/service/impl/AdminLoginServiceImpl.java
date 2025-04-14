package com.piggod.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.piggod.common.domain.dto.UserDTO;
import com.piggod.common.domain.po.LoginUser;
import com.piggod.common.domain.po.User;
import com.piggod.common.domain.vo.*;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.exception.SystemException;
import com.piggod.common.service.IAdminLoginService;
import com.piggod.common.service.IBlogLoginService;
import com.piggod.common.service.IMenuService;
import com.piggod.common.service.IRoleService;
import com.piggod.common.utils.JwtUtil;
import com.piggod.common.utils.RedisCache;
import com.piggod.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static com.piggod.common.constants.SystemConstants.ADMIN_LOGIN_KEY_PREFIX;

@Service
public class AdminLoginServiceImpl implements IAdminLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IMenuService menuService;



    @Override
    public ResponseResult login(UserDTO userDTO) {
        if (Objects.isNull(userDTO)) {
            throw new SystemException(AppHttpCodeEnum.LOGIN_INFO_NOT_NULL);
        }

        if (!StringUtils.hasText(userDTO.getUserName()) || !StringUtils.hasText(userDTO.getPassword())) {
            // 用户名或密码为空
            throw new SystemException(
                    StringUtils.hasText(userDTO.getUserName()) ?
                            AppHttpCodeEnum.REQUIRE_PASSWORD : AppHttpCodeEnum.REQUIRE_USERNAME
            );

        }


        // 1.调用AuthenticationManager实现类的authenticate方法进行登录认证
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(userDTO.getUserName(), userDTO.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 2.校验数据
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        if (Objects.isNull(loginUser)) {
            throw new SystemException(AppHttpCodeEnum.LOGIN_ERROR);
        }

        // 3.通过则生成token并存入redis 并且封装返回用户信息
        Long userId = loginUser.getUser().getId();
        String jwt = JwtUtil.createJWT(userId.toString());

        // 用户信息存入redis
        redisCache.setCacheObject(ADMIN_LOGIN_KEY_PREFIX+ userId, loginUser);

        AdminUserLoginVO adminUserLoginVO = new AdminUserLoginVO(jwt);


        return ResponseResult.okResult(adminUserLoginVO);
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
        redisCache.deleteObject(ADMIN_LOGIN_KEY_PREFIX + userId);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult<AdminUserInfoVO> getAdminUserInfoVO() {
        // 1.获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();

        if (ObjectUtil.isNull(loginUser)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        User user = loginUser.getUser();
        UserInfoVO userInfo = BeanUtil.toBean(user, UserInfoVO.class);

        // 2.查询权限信息
        List<String> perms = menuService.selectPermsByUserId(user.getId());

        // 3.查询角色信息
        List<String> roles = roleService.selectRoleKeyByUserId(user.getId());

        // 4.封装返回
        AdminUserInfoVO adminUserInfoVO = new AdminUserInfoVO(perms, roles, userInfo);
        return ResponseResult.okResult(adminUserInfoVO);
    }

    @Override
    public ResponseResult<RoutersVO> getRouters() {
//        响应格式如下: 前端为了实现动态路由的效果，需要后端有接口能返回用户所能访问的菜单数据。注意: 返回的菜单数据需要体现父子菜单的层级关系
//        如果用户id为1代表管理员，menus中需要有所有菜单类型为C或者M的，C表示菜单，M表示目录，状态为正常的，未被删除的权限
        // *id为1为超级管理员
        // 1.获取当前登录用户
        Long userId = SecurityUtils.getUserId();
        List<MenuVO> menus = menuService.selectMenusUserId(userId);

        RoutersVO routersVO = new RoutersVO(menus);


        return ResponseResult.okResult(routersVO);
    }
}
