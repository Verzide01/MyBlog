package com.piggod.common.service;

import com.piggod.common.domain.dto.AddRoleDTO;
import com.piggod.common.domain.dto.ChangeRoleStatusDTO;
import com.piggod.common.domain.dto.UpdateRoleDTO;
import com.piggod.common.domain.po.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.piggod.common.domain.query.RolePageQuery;
import com.piggod.common.domain.vo.ResponseResult;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author 梁峰
 * @since 2025-04-13
 */
public interface IRoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult listRoleByPage(RolePageQuery query);

    ResponseResult addRole(AddRoleDTO addRoleDTO);

    ResponseResult getRoleById(Long id);

    ResponseResult updateRole(UpdateRoleDTO updateRoleDTO);

    ResponseResult deleteRoleById(Long[] id);

    ResponseResult changeRoleStatus(ChangeRoleStatusDTO changeRoleStatusDTO);

    ResponseResult listAllRole();
}
