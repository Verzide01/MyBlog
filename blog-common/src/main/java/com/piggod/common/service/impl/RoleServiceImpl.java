package com.piggod.common.service.impl;

import com.piggod.common.constants.SystemConstants;
import com.piggod.common.domain.po.Role;
import com.piggod.common.mapper.RoleMapper;
import com.piggod.common.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.piggod.common.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.piggod.common.constants.SystemConstants.USER_IS_ADMIN;

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
}
