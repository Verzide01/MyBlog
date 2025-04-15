package com.piggod.common.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTagDTO {

    @NotNull(message = "ID不能为空")
    private Long id;
    //备注
    @NotBlank(message = "备注不能为空")
    private String remark;
    //标签名
    @NotBlank(message = "标签名不能为空")
    private String name;
}