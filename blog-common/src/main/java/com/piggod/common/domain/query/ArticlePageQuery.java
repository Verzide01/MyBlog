package com.piggod.common.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "文章分页查询条件")
public class ArticlePageQuery extends PageQuery {
    @ApiModelProperty(value = "分类id")
    private Long categoryId;
}
