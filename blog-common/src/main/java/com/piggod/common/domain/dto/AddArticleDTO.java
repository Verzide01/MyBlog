package com.piggod.common.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//新增博客文章
public class AddArticleDTO {

    //标题
    @NotEmpty(message = "标题不能为空")
    private String title;
    //文章内容
    @NotEmpty(message = "文章内容不能为空")
    private String content;
    //文章摘要
    @NotEmpty(message = "文章摘要不能为空")
    private String summary;
    //所属分类id
    @NotNull(message = "分类不能为空")
    private Long categoryId;

    //缩略图
    private String thumbnail;
    //是否置顶（0否，1是）
    @NotEmpty(message = "是否置顶不能为空")
    private String isTop;
    //状态（0已发布，1草稿）
    @NotEmpty(message = "文章状态不能为空")
    private String status;
    @NotEmpty(message = "是否允许评论不能为空")
    //是否允许评论 1是，0否
    private String isComment;

    //tags属性是一个List集合，用于接收文章关联标签的id
    private List<Long> tags;
}
