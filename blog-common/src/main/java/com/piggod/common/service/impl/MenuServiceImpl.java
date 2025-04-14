package com.piggod.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.piggod.common.domain.po.Menu;
import com.piggod.common.domain.vo.MenuVO;
import com.piggod.common.mapper.MenuMapper;
import com.piggod.common.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.piggod.common.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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
                    .select(Menu::getPerms)
                    .in(Menu::getMenuType, TYPE_MENU, TYPE_BUTTON)
                    .eq(Menu::getStatus, PERMISSIONS_STATUS_NORMAL)
                    .list();

            if (CollUtil.isEmpty(menuList)) {
                return ListUtil.empty();
            }

            List<String> perms = menuList.stream().map(Menu::getPerms).collect(Collectors.toList());
            return perms;
        }

        // 1.根据userId查询 用户角色关联表 得到角色信息
        // 2.根据角色信息 查询权限菜单关联表 得到权限菜单
        // 3.根据权限菜单 得到 菜单信息
        List<String> perms = getBaseMapper().selectPermsByUserId(id);
        return perms;
    }

    @Override
    public List<MenuVO> selectMenusUserId(Long userId) {
        // *如果id为1则是超级管理员直接返回所有权限
        if (SecurityUtils.isAdmin()) {
            // 根菜单
            List<Menu> menuList = lambdaQuery()
                    .in(Menu::getMenuType, TYPE_MENU, TYPE_CATEGORY)
                    .eq(Menu::getStatus, PERMISSIONS_STATUS_NORMAL)
                    .orderByAsc(Menu::getParentId, Menu::getOrderNum)
                    .list();

            List<MenuVO> menuVoList = getMenuVoList(menuList);
            return menuVoList;
        }

        // 普通用户连表查找数据库
        List<Menu> menuList = getBaseMapper().selectMenusUserId(userId);

        List<MenuVO> menuVoList = getMenuVoList(menuList);

        return menuVoList;
    }


    /**
     * 将没有层级关系的列表转化成父（partId为0的）为根
     * 然后获取根的id，查找map对应的根id对应的子
     * 封装成父子关系
     * @param menuList poList
     * @return voList
     */
    private List<MenuVO> getMenuVoList(List<Menu> menuList) {
        if (CollUtil.isEmpty(menuList)) {
            return ListUtil.empty();
        }

        List<MenuVO> voList = BeanUtil.copyToList(menuList, MenuVO.class);

        // 子菜单 key为parentID value为子菜单
        Map<Long, List<MenuVO>> menuVoMap = voList.stream().collect(Collectors.groupingBy(MenuVO::getParentId));

        // 根
        List<MenuVO> rootMenus = menuVoMap.get(MENU_ROOT_ID);

        setChildren(rootMenus, menuVoMap);
        return rootMenus;
    }

    private void setChildren(List<MenuVO> rootMenus, Map<Long, List<MenuVO>> menuVoMap) {
        rootMenus.forEach(menu -> {
            List<MenuVO> childMenuVoList = menuVoMap.get(menu.getId());// 在map中查找为根id的菜单
            //假如有三层的话可以加这个进行递归
//            if (CollUtil.isNotEmpty(childMenuVoList)) {
//            setChildren(childMenuVoList, menuVoMap);
//            }
            menu.setChildren(childMenuVoList);
        });
    }
}
