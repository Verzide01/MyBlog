package com.piggod.common.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode()
@Data
@NotNull(message = "内容不能为空")
public class UserDTO {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;
}
