package com.piggod.common.service;

import com.piggod.common.domain.dto.AddTagDTO;
import com.piggod.common.domain.dto.UpdateTagDTO;
import com.piggod.common.domain.po.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.piggod.common.domain.query.TagPageQuery;
import com.piggod.common.domain.vo.ResponseResult;

/**
 * <p>
 * 标签 服务类
 * </p>
 *
 * @author 梁峰
 * @since 2025-04-13
 */
public interface ITagService extends IService<Tag> {

    ResponseResult listByPage(TagPageQuery tagPageQuery);

    ResponseResult getTagById(Long id);

    ResponseResult addTag(AddTagDTO addTagDTO);

    ResponseResult deleteTag(Long[] id);

    ResponseResult updateTag(UpdateTagDTO updateTagDto);

    ResponseResult listAllTag();

}
