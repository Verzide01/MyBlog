package com.piggod.blogadmin.controller;

import com.piggod.common.annotation.SystemLog;
import com.piggod.common.domain.dto.*;
import com.piggod.common.domain.query.LinkPageQuery;
import com.piggod.common.domain.query.RolePageQuery;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @SystemLog(bussinessName = "后台条件查询角色信息")
    @GetMapping("/list")
    public ResponseResult listRoleByPage(@Valid RolePageQuery query ){
        return roleService.listRoleByPage(query);
    }

    @SystemLog(bussinessName = "后台新增角色")
    @PostMapping
    public ResponseResult addRole(@RequestBody @Valid AddRoleDTO addRoleDTO){
        return roleService.addRole(addRoleDTO);
    }

    @SystemLog(bussinessName = "后台修改角色时根据id查询角色")
    @GetMapping("/{id}")
    public ResponseResult getRoleById(@PathVariable @Min(value = 1L, message = "标签最小值为1") Long id){
        return roleService.getRoleById(id);
    }

    @SystemLog(bussinessName = "后台更新角色")
    @PutMapping
    public ResponseResult updateRole(@RequestBody @Valid UpdateRoleDTO updateRoleDTO){
        return roleService.updateRole(updateRoleDTO);
    }

    @SystemLog(bussinessName = "后台删除角色")
    @DeleteMapping("/{id}")
    public ResponseResult deleteRoleById(@PathVariable @NotBlank(message = "标签最小值为1") Long... id){
        return roleService.deleteRoleById(id);
    }

    @SystemLog(bussinessName = "后台改变角色状态")
    @PutMapping("/changeStatus")
    public ResponseResult changeRoleStatus(@RequestBody @Valid ChangeRoleStatusDTO changeRoleStatusDTO){
        return roleService.changeRoleStatus(changeRoleStatusDTO);
    }

    @SystemLog(bussinessName = "后台-新增用户时-查询所有正常的角色")
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }



}
