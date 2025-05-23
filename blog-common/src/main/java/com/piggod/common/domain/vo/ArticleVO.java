package com.piggod.common.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArticleVO {
    private Long id;
    //标题
    private String title;
    //文章摘要
    private String summary;
    // 所属分类id
    private Long categoryId;
    //所属分类名
    private String categoryName;
    //文章内容
    private String content;
    //缩略图
    private String thumbnail;
    //访问量
    private Long viewCount;

    private Date createTime;



    //是否置顶（0否，1是）
    private String isTop;
    //状态（0已发布，1草稿）
    private String status;

    //是否允许评论 1是，0否
    private String isComment;
    private List<Long> tags;


}
