package com.piggod.blogadmin.controller;

import com.piggod.common.annotation.SystemLog;
import com.piggod.common.domain.dto.AddUserDTO;
import com.piggod.common.domain.dto.UpdateUserDTO;
import com.piggod.common.domain.query.UserPageQuery;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("system/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @SystemLog(bussinessName = "后台条件查询用户信息")
    @GetMapping("/list")
    public ResponseResult listUserByPage(@Valid UserPageQuery query ){
        return userService.listUserByPage(query);
    }

    @SystemLog(bussinessName = "后台添加用户角色")
    @PostMapping
    public ResponseResult addUser(@RequestBody @Valid AddUserDTO addUserDTO){
        return userService.addUser(addUserDTO);
    }

    @SystemLog(bussinessName = "后台修改用户时根据id查询用户")
    @GetMapping("/{id}")
    public ResponseResult getUserById(@PathVariable @Min(value = 1L, message = "标签最小值为1") Long id){
        return userService.getUserById(id);
    }

    @SystemLog(bussinessName = "后台更新用户")
    @PutMapping
    public ResponseResult updateUser(@RequestBody @Valid UpdateUserDTO updateUserDTO){
        return userService.updateUser(updateUserDTO);
    }

    @SystemLog(bussinessName = "后台删除用户")
    @DeleteMapping("/{id}")
    public ResponseResult deleteUserById(@PathVariable @NotBlank(message = "标签最小值为1") Long... id){
        return userService.deleteUserById(id);
    }
}
