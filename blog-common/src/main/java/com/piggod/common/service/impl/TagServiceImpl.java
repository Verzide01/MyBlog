package com.piggod.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.piggod.common.domain.dto.AddTagDTO;
import com.piggod.common.domain.dto.PageDTO;
import com.piggod.common.domain.dto.UpdateTagDto;
import com.piggod.common.domain.po.Tag;
import com.piggod.common.domain.query.TagPageQuery;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.domain.vo.TagVO;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.exception.SystemException;
import com.piggod.common.mapper.TagMapper;
import com.piggod.common.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 标签 服务实现类
 * </p>
 *
 * @author 梁峰
 * @since 2025-04-13
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    @Override
    public ResponseResult listByPage(TagPageQuery query) {
        if (ObjUtil.isEmpty(query)){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
//        if (StrUtil.isBlank(query.getName()) && StrUtil.isEmpty(query.getRemark())){
//            throw new SystemException(AppHttpCodeEnum.QUERY_IS_NULL);
//        }
        // 1.分页查询标签
        Page<Tag> page = lambdaQuery()
                .like(StrUtil.isNotEmpty(query.getRemark()), Tag::getRemark, query.getRemark())
//                .or() 看要求 不适用or默认是and
                .like(StrUtil.isNotEmpty(query.getName()), Tag::getName, query.getName())
                .page(query.toMpPage());

        List<Tag> tags = page.getRecords();
        if (tags.isEmpty()){
            return ResponseResult.okResult(PageDTO.empty(page));
        }

        List<TagVO> voList = BeanUtil.copyToList(tags, TagVO.class);

        return ResponseResult.okResult(PageDTO.of(page, voList));
    }

    @Override
    public ResponseResult getTagById(Long id) {
        return null;
    }

    @Override
    public ResponseResult addTag(AddTagDTO addTagDTO) {
        return null;
    }

    @Override
    public ResponseResult deleteTag(Long id) {
        return null;
    }

    @Override
    public ResponseResult updateTag(UpdateTagDto updateTagDto) {
        return null;
    }
}
