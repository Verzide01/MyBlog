package com.piggod.common.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class SelectMenuDTO {

    /**
     * 菜单状态（0正常 1停用）
     */
    private String status;

    /**
     * 菜单名称
     */
    private String menuName;
}
