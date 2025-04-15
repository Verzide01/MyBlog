package com.piggod.common.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "文章分页查询条件")
public class ArticlePageQuery extends PageQuery {
    @ApiModelProperty(value = "分类id")
    @Min(value = 1L, message = "分类id要大于1")
    private Long categoryId;

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "文章摘要")
    private String summary;
}
