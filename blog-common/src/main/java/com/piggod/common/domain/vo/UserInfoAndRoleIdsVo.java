package com.piggod.common.domain.vo;

import com.piggod.common.domain.po.Role;
import com.piggod.common.domain.po.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoAndRoleIdsVo {
    private User user;
    private List<RoleVO> roles;
    private Set<Long> roleIds;
}