package com.piggod.common.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryDTO {
    private Integer id;
    //分类名
    @NotBlank(message = "分类名不能为空")
    private String name;
    //描述
    @NotBlank(message = "描述不能为空")
    private String description;
    //状态0:正常,1禁用
    @NotBlank(message = "状态不能为空")
    private String status;
}
