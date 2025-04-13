package com.piggod.common.service.impl;

import com.piggod.common.constants.SystemConstants;
import com.piggod.common.domain.po.Menu;
import com.piggod.common.mapper.MenuMapper;
import com.piggod.common.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.piggod.common.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.piggod.common.constants.SystemConstants.*;


/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author 梁峰
 * @since 2025-04-13
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<String> selectPermsByUserId(Long id) {
        // *如果id为1则是超级管理员直接返回所有权限
        if (SecurityUtils.isAdmin()){
            List<Menu> menuList = lambdaQuery()
                    .in(Menu::getMenuType, TYPE_MENU, TYPE_BUTTON)
                    .eq(Menu::getStatus, PERMISSIONS_STATUS_NORMAL)
                    .list();
            List<String> perms = menuList.stream().map(Menu::getPerms).collect(Collectors.toList());
            return perms;
        }

        // 1.根据userId查询 用户角色关联表 得到角色信息
        // 2.根据角色信息 查询权限菜单关联表 得到权限菜单
        // 3.根据权限菜单 得到 菜单信息
        List<String> perms = getBaseMapper().selectPermsByUserId(id);
        return perms;
    }
}
