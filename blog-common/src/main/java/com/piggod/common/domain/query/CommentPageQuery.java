package com.piggod.common.domain.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "评论分页查询条件")
public class CommentPageQuery extends PageQuery {
    @ApiModelProperty(value = "文章id")
    @Min(value = 1L, message = "文章id不能小于1")
    private Long articleId;

    @ApiModelProperty(value = "评论类型，0为友链评论，1为文章评论")
    private String type;
}
