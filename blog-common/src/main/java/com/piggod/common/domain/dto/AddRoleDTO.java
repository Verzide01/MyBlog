package com.piggod.common.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddRoleDTO {


    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @NotNull(message = "角色名称不能为空")
    private String roleName;

    /**
     * 角色权限字符串
     */
    @NotBlank(message = "权限字符不能为空")
    @NotNull(message = "权限字符不能为空")
    private String roleKey;

    /**
     * 显示顺序
     */
    @Min(value = 1, message = "显示排序最小值为1")
    @NotNull(message = "显示排序不能为空")
    private Integer roleSort;

    /**
     * 角色状态（0正常 1停用）
     */
    @Pattern(regexp = "0|1", message = "角色状态只能为 0 或 1")
    private String status;


    /**
     * 关联菜单权限列表
     */
    private List<Long> menuIds;



    /**
     * 备注
     */
    private String remark;
}
