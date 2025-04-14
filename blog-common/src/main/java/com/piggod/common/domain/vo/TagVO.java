package com.piggod.common.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class TagVO {

    private String id;

    private String name;

    private String remark;
}
