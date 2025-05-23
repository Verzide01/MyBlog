package com.piggod.common.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryVO {
    private Long id;
    // 分类名
    private String name;

    // 描述
    private String description;

    private String status;

    private Long pid;
}
