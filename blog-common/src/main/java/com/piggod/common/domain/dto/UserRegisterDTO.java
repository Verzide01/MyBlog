package com.piggod.common.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;

@EqualsAndHashCode()
@Data
public class UserRegisterDTO {

    @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$", message = "用户名格式错误")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @Size(min = 1, max = 20, message = "昵称长度需为1-20位")
    @NotBlank(message = "昵称不能为空")
    private String nickName;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "密码需至少8位，包含大小写字母、数字和特殊符号")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Email(message = "邮箱格式错误")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错")
    private String phonenumber;

}
