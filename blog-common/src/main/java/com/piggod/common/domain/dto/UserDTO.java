package com.piggod.common.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode()
@Data
@NotNull(message = "内容不能为空")
public class UserDTO {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @NotNull(message = "用户名不能为空")
    private String userName;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @NotNull(message = "密码不能为空")
    private String password;
}
