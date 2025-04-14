package com.piggod.common.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserInfoVO {

    private List<String> permissions;

    private List<String> roles;

    private UserInfoVO user;
}
