package com.piggod.common.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserDTO {

    /**
     * 用户名
     */
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$", message = "用户名格式只能是数字、字母、下划线组合")
    @NotBlank(message = "用户名不能为空")
    private String userName;

    /**
     * 昵称
     */
    @Size(min = 1, max = 20, message = "昵称长度需为1-20位")
    @NotBlank(message = "昵称不能为空")
    private String nickName;

    /**
     * 密码
     */
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "密码需至少8位，包含大小写字母、数字和特殊符号")
    @NotBlank(message = "密码不能为空")
    private String password;



    /**
     * 账号状态（0正常 1停用）
     */
    @Pattern(regexp = "0|1", message = "账号状态只能为 0 或 1")
    private String status;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式错误")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错")
    private String phonenumber;

    /**
     * 用户性别（0男，1女，2未知）
     */
    @NotBlank(message = "用户性别不能为空")
    @NotNull(message = "用户性别不能为空")
    @Pattern(regexp = "0|1", message = "性别只能为 0 或 1")
    private String sex;


    private List<Long> roleIds;
}
