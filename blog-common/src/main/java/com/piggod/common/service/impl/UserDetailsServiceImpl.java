package com.piggod.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.piggod.common.constants.SystemConstants;
import com.piggod.common.domain.po.LoginUser;
import com.piggod.common.domain.po.User;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.exception.SystemException;
import com.piggod.common.mapper.MenuMapper;
import com.piggod.common.mapper.UserMapper;
import com.piggod.common.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private IMenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.根据用户名查询用户信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(wrapper);

        // 2.校验数据
        if (Objects.isNull(user)) {
            throw new SystemException(AppHttpCodeEnum.LOGIN_ERROR);
        }

        // 3.返回用户信息
        // 获取权限信息封装
        // 判断是否后台用户    后台用户管理员才查询权限信息
        if (SystemConstants.IS_ADMIN.equals(user.getType())){
            // 该用户为管理员
            List<String> perms = menuMapper.selectPermsByUserId(user.getId());
//            List<String> perms = menuService.selectPermsByUserId(user.getId()); // 错误的 因为调用了Security里面的getLoginUser方法 都没登陆完成能怎么能掉 应该直接调用mapper数据库
            return new LoginUser(user, perms);
        }

        // 否则为普通用户 直接返回
        return new LoginUser(user, ListUtil.empty());
    }
}
