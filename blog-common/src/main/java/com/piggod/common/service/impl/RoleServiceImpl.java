package com.piggod.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.piggod.common.constants.SystemConstants;
import com.piggod.common.domain.dto.AddRoleDTO;
import com.piggod.common.domain.dto.ChangeRoleStatusDTO;
import com.piggod.common.domain.dto.PageDTO;
import com.piggod.common.domain.dto.UpdateRoleDTO;
import com.piggod.common.domain.po.*;
import com.piggod.common.domain.query.RolePageQuery;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.domain.vo.RoleVO;
import com.piggod.common.domain.vo.TagVO;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.exception.SystemException;
import com.piggod.common.mapper.RoleMapper;
import com.piggod.common.service.IRoleMenuService;
import com.piggod.common.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.piggod.common.service.IUserRoleService;
import com.piggod.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.piggod.common.constants.SystemConstants.*;
import static com.piggod.common.enums.AppHttpCodeEnum.*;
import static com.piggod.common.enums.AppHttpCodeEnum.SUCCESS;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author 梁峰
 * @since 2025-04-13
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private IRoleMenuService roleMenuService;
    @Autowired
    private IUserRoleService userRoleService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        // *如果id为1则是超级管理员直接返回admin
        if (SecurityUtils.isAdmin()){
            return List.of(USER_IS_ADMIN);
        }

        // 1.根据userId查询查询关联表中对应的角色
        // 2.查询角色表的角色信息封装
        List<String> roleKeyList = getBaseMapper().selectRoleKeyByUserId(id);
        return roleKeyList;
    }

    @Override
    public ResponseResult listRoleByPage(RolePageQuery query) {
        if (ObjUtil.isEmpty(query)){
            throw new SystemException(SYSTEM_ERROR);
        }

        // 1.分页查询权限
        Page<Role> page = lambdaQuery()
                .like(StrUtil.isNotEmpty(query.getRoleName()), Role::getRoleName, query.getRoleName())
                .eq(StrUtil.isNotEmpty(query.getStatus()), Role::getStatus, query.getStatus())
                .orderByAsc(Role::getRoleSort)
                .page(query.toMpPage());

        List<Role> roles = page.getRecords();
        if (roles.isEmpty()){
            return ResponseResult.okResult(PageDTO.empty(page));
        }

        List<RoleVO> voList = BeanUtil.copyToList(roles, RoleVO.class);

        return ResponseResult.okResult(PageDTO.of(page, voList));
    }

    @Override
    @Transactional
    public ResponseResult addRole(AddRoleDTO addRoleDTO) {
        if (ObjUtil.isEmpty(addRoleDTO)){
            throw new SystemException(PARAM_INVALID);
        }

        Role role = BeanUtil.toBean(addRoleDTO, Role.class);
        boolean save = save(role);

        if (!save){
            throw new SystemException(ADD_UNSUCCESS);
        }

        // 保存成功则添加数据到关联表
        List<Long> menuIds = addRoleDTO.getMenuIds();
        if (menuIds.isEmpty()){
            // 说明为空不需要保存中间表
            return ResponseResult.okResult(SUCCESS);
        }

        List<RoleMenu> roleMenuList = menuIds.stream()
                .map(menuId -> new RoleMenu(role.getId(), menuId)).collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);

        return ResponseResult.okResult(SUCCESS);
    }

    @Override
    public ResponseResult getRoleById(Long id) {
        if (ObjectUtil.isNull(id) || VALUE_IS_ZERO.equals(id)){
            throw new SystemException(VALUE_LITTLE_MIN_NUM);
        }

        Role role = lambdaQuery()
                .eq(id != null, Role::getId, id)
                .one();
        RoleVO roleVO = BeanUtil.toBean(role, RoleVO.class);
        return ResponseResult.okResult(roleVO);
    }

    @Override
    @Transactional  //涉及两张表更新、修改操作
    public ResponseResult updateRole(UpdateRoleDTO updateRoleDTO) {
        if (ObjUtil.isEmpty(updateRoleDTO)) {
            throw new SystemException(SYSTEM_ERROR);
        }

        Role role = BeanUtil.toBean(updateRoleDTO, Role.class);
        boolean update = updateById(role);

        if (!update) {
            throw new SystemException(UPDATE_UNSUCCESS);
        }
        // 更新文章信息成功 删除关联标签
        List<Long> menuIds = updateRoleDTO.getMenuIds();

        // 删除旧的标签关联（无论 menuIds 是否为空）
        LambdaQueryWrapper<RoleMenu> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(RoleMenu::getRoleId, role.getId());
        roleMenuService.remove(deleteWrapper);

        // 插入新的标签关联（如果 menuIds 非空）
        if (CollUtil.isNotEmpty(menuIds)) {
            List<RoleMenu> newRoleMenus = menuIds.stream()
                    .map(menuId -> new RoleMenu(role.getId(), menuId))
                    .collect(Collectors.toList());
            roleMenuService.saveBatch(newRoleMenus);
        }

        return ResponseResult.okResult(SUCCESS);

    }

    @Override
    @Transactional //三张表删除
    public ResponseResult deleteRoleById(Long[] id) {
        for (Long tagId : id) {
            if (tagId < VALUE_MIN_NUM){
                throw new SystemException(AppHttpCodeEnum.VALUE_LITTLE_MIN_NUM);
            }
        }

        List<Long> ids = ListUtil.toList(id);
        boolean remove = removeByIds(ids);
        if (!remove){
            throw new SystemException(DELETE_UNSUCCESS);
        }
        // 删除中间表
        // role_menu表
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(RoleMenu::getRoleId, ids);

        // user_role表
        LambdaQueryWrapper<UserRole> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.in(UserRole::getRoleId, ids);

        roleMenuService.remove(wrapper);
        userRoleService.remove(wrapper2);

        return ResponseResult.okResult(SUCCESS);
    }

    @Override
    public ResponseResult changeRoleStatus(ChangeRoleStatusDTO changeRoleStatusDTO) {
        if (ObjUtil.isEmpty(changeRoleStatusDTO)){
            throw new SystemException(PARAM_INVALID);
        }

        Role role = new Role(changeRoleStatusDTO.getRoleId(), changeRoleStatusDTO.getStatus());
        boolean update = updateById(role);
        if (!update){
            throw new SystemException(UPDATE_UNSUCCESS);
        }

        return ResponseResult.okResult(SUCCESS);

    }

    @Override
    public ResponseResult listAllRole() {

        List<Role> list = lambdaQuery()
                .eq(Role::getStatus, ROLE_STATUS_NORMAL)
                .orderByAsc(Role::getRoleSort)
                .list();

        List<RoleVO> voList = BeanUtil.copyToList(list, RoleVO.class);
        return ResponseResult.okResult(voList);

    }
}
