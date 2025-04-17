package com.piggod.common.domain.query;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "分类分页查询条件")
public class CategoryPageQuery extends PageQuery {
}
