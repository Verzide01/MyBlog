package com.piggod.common.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddLinkDTO {

    @NotBlank(message = "分类名不能为空")
    private String name;

    @NotBlank(message = "分类名不能为空")
    private String logo;

    @NotBlank(message = "分类名不能为空")
    private String description;

    /**
     * 网站地址
     */
    @NotBlank(message = "分类名不能为空")
    private String address;

    /**
     * 审核状态 (0代表审核通过，1代表审核未通过，2代表未审核)
     */
    @NotBlank(message = "分类名不能为空")
    private String status;
}
