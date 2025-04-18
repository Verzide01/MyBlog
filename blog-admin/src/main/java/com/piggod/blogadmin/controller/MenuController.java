package com.piggod.blogadmin.controller;


import com.piggod.common.annotation.SystemLog;
import com.piggod.common.domain.dto.AddMenuDTO;
import com.piggod.common.domain.dto.SelectMenuDTO;
import com.piggod.common.domain.dto.UpdateMenuDTO;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @SystemLog(bussinessName = "查询权限菜单")
    @GetMapping("/list")
    public ResponseResult selectMenuList(SelectMenuDTO menuDTO) {
        return menuService.selectMenuList(menuDTO);
    }

    @SystemLog(bussinessName = "新增菜单")
    @PostMapping
    public ResponseResult addMenu(@RequestBody @Valid AddMenuDTO addMenuDTO) {
        return menuService.addMenu(addMenuDTO);
    }

    @SystemLog(bussinessName = "修改菜单时查询菜单信息")
    @GetMapping("/{menuId}")
    public ResponseResult getMenuById(@PathVariable @Min(value = 1L, message = "标签最小值为1") Long menuId) {
        return menuService.getMenuById(menuId);
    }

    @SystemLog(bussinessName = "修改菜单")
    @PutMapping
    public ResponseResult updateMenu(@RequestBody @Valid UpdateMenuDTO updateMenuDTO){
        return menuService.updateMenu(updateMenuDTO);
    }

    @SystemLog(bussinessName = "删除菜单")
    @DeleteMapping("/{id}")
    public ResponseResult deleteMenuById(@PathVariable @NotBlank(message = "标签最小值为1") Long... id){
        return menuService.deleteMenuById(id);
    }


    @SystemLog(bussinessName = "后台-角色管理-查询权限菜单树")
    @GetMapping("/treeselect")
    public ResponseResult selectMenuTree(){
        return menuService.selectMenuTree();
    }

    @SystemLog(bussinessName = "后台-角色管理-修改角色信息时-查询角色id对应权限菜单树")
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeselect(@PathVariable @NotBlank(message = "标签最小值为1") Long id){
        return menuService.roleMenuTreeselect(id);
    }
}
