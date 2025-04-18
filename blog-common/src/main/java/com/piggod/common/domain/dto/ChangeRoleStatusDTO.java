package com.piggod.common.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeRoleStatusDTO {

    @Min(value = 1L, message = "角色id不能小于1")
    private Long roleId;

//    @Min(value = 1L, message = "角色id不能小于1")
//    private Long id = roleId;

    @NotBlank(message = "状态不能为空")
    private String status;

}