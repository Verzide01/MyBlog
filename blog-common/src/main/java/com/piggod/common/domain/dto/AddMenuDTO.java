package com.piggod.common.domain.dto;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMenuDTO {


    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    @NotNull(message = "菜单名称不能为空")
    private String menuName;

    /**
     * 父菜单ID
     */
    @Min(value = 1, message = "父菜单ID最小值为1")
    private Long parentId;

    /**
     * 显示顺序
     */
    @Min(value = 1, message = "显示排序最小值为1")
    @NotNull(message = "显示排序不能为空")
    private Integer orderNum;

    /**
     * 路由地址
     */
    @NotBlank(message = "路由地址不能为空")
    @NotNull(message = "路由地址不能为空")
    private String path;

    /**
     * 组件路径
     */
//    @NotBlank(message = "组件路径不能为空") 当menuType为C是可以为空， 这个直接在业务代码里校验
    private String component;

    /**
     * 是否为外链（0是 1否）
     */
    @Min(value = 0, message = "是否为外链 只能为 0 或 1")
    @Max(value = 1, message = "是否为外链 只能为 0 或 1")
    private Integer isFrame;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    @NotBlank(message = "菜单类型不能为空")
    @Pattern(regexp = "M|C|F", message = "菜单类型只能为 M（目录）、C（菜单）或 F（按钮）")
    private String menuType;

    /**
     * 显示状态（0显示 1隐藏）
     */
    @Pattern(regexp = "0|1", message = "显示状态只能为 0 或 1")
    private String visible;

    /**
     * 菜单状态（0正常 1停用）
     */
    @Pattern(regexp = "0|1", message = "菜单状态只能为 0 或 1")
    private String status;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;


    private String remark;

}
