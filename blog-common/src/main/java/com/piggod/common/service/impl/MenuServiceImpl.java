package com.piggod.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.piggod.common.constants.SystemConstants;
import com.piggod.common.domain.dto.AddMenuDTO;
import com.piggod.common.domain.dto.SelectMenuDTO;
import com.piggod.common.domain.dto.UpdateMenuDTO;
import com.piggod.common.domain.po.ArticleTag;
import com.piggod.common.domain.po.Menu;
import com.piggod.common.domain.po.Tag;
import com.piggod.common.domain.vo.MenuVO;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.domain.vo.TagVO;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.exception.SystemException;
import com.piggod.common.mapper.MenuMapper;
import com.piggod.common.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.piggod.common.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.piggod.common.constants.SystemConstants.*;
import static com.piggod.common.enums.AppHttpCodeEnum.*;


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

    @Override
    public ResponseResult selectMenuList(SelectMenuDTO selectMenuDTO) {

        // 1.条件查询数据库
        List<Menu> menuList = lambdaQuery()
                .like(StrUtil.isNotBlank(selectMenuDTO.getMenuName()), Menu::getMenuName, selectMenuDTO.getMenuName())
                .eq(StrUtil.isNotBlank(selectMenuDTO.getStatus()), Menu::getStatus, selectMenuDTO.getStatus())
                .orderByAsc(Menu::getOrderNum)
                .list();

        if (CollUtil.isEmpty(menuList)) {
            return ResponseResult.okResult(ListUtil.empty());
        }

        // 2.封装vo
        List<MenuVO> menuVoList = BeanUtil.copyToList(menuList, MenuVO.class);

        return ResponseResult.okResult(menuVoList);
    }

    @Override
    public ResponseResult addMenu(AddMenuDTO addMenuDTO) {
        if (ObjUtil.isEmpty(addMenuDTO)){
            throw new SystemException(SYSTEM_ERROR);
        }

        if (TYPE_MENU.equals(addMenuDTO.getMenuType()) && (addMenuDTO.getComponent() == null || addMenuDTO.getComponent().trim().isEmpty())) {
            throw new SystemException(COMPONENT_NOT_NULL_WHEN_HAS_TYPE_MENU);
        }

        Menu menu = BeanUtil.toBean(addMenuDTO, Menu.class);
        boolean save = save(menu);

        if (!save){
            throw new SystemException(ADD_UNSUCCESS);
        }

        return ResponseResult.okResult(SUCCESS);
    }

    @Override
    public ResponseResult getMenuById(Long menuId) {
        if (ObjectUtil.isNull(menuId) || VALUE_IS_ZERO.equals(menuId)){
            throw new SystemException(VALUE_LITTLE_MIN_NUM);
        }


        Menu menu = lambdaQuery()
                .eq(menuId != null, Menu::getId, menuId)
                .one();
        MenuVO menuVO = BeanUtil.toBean(menu, MenuVO.class);
        return ResponseResult.okResult(menuVO);
    }

    @Override
    public ResponseResult updateMenu(UpdateMenuDTO updateMenuDTO) {
        if (ObjUtil.isEmpty(updateMenuDTO)){
            throw new SystemException(PARAM_INVALID);
        }

        if (NumberUtil.equals(updateMenuDTO.getParentId(), updateMenuDTO.getId())){
            return ResponseResult.okResult(MENU_NOT_BE_CHANGE_SELF.getCode(),
                    "修改菜单" + "'" +  updateMenuDTO.getMenuName() + "'" + "失败，上级菜单不能选择自己");
        }


        Menu menu = BeanUtil.toBean(updateMenuDTO, Menu.class);

        boolean update = updateById(menu);
        if (!update){
            throw new SystemException(UPDATE_UNSUCCESS);
        }

        return ResponseResult.okResult(SUCCESS);
    }

    @Override
    public ResponseResult deleteMenuById(Long[] id) {
        for (Long tagId : id) {
            if (tagId < VALUE_MIN_NUM){
                throw new SystemException(VALUE_LITTLE_MIN_NUM);
            }
        }


        boolean hasChild = hasChild(id);
        if (hasChild){
            throw new SystemException(EXIST_CHILDREN_MENU);
        }

        List<Long> ids = ListUtil.toList(id);

        boolean remove = removeByIds(ids);
        if (!remove){
            throw new SystemException(DELETE_UNSUCCESS);
        }


        return ResponseResult.okResult(SUCCESS);
    }

    @Override
    public boolean hasChild(Long[] menuId) {
        List<Long> idList = ListUtil.toList(menuId);

        Integer count = lambdaQuery()
                .in(Menu::getParentId, idList)
                .count();

        return count > 0;
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
