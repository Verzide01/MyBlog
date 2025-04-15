package com.piggod.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.piggod.common.constants.SystemConstants;
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

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

import static com.piggod.common.constants.SystemConstants.VALUE_MIN_NUM;
import static com.piggod.common.enums.AppHttpCodeEnum.*;

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
            throw new SystemException(SYSTEM_ERROR);
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
        Tag tag = lambdaQuery()
                .eq(id != null, Tag::getId, id)
                .one();
        TagVO tagVO = BeanUtil.toBean(tag, TagVO.class);
        return ResponseResult.okResult(tagVO);
    }

    @Override
    public ResponseResult addTag(AddTagDTO addTagDTO) {
        if (ObjUtil.isEmpty(addTagDTO)){
            throw new SystemException(SYSTEM_ERROR);
        }

        Tag tag = BeanUtil.toBean(addTagDTO, Tag.class);
        boolean save = save(tag);

        if (!save){
            throw new SystemException(ADD_UNSUCCESS);
        }

        return ResponseResult.okResult(SUCCESS);
    }

    @Override
    public ResponseResult deleteTag(Long[] id) {
        for (Long tagId : id) {
            if (tagId < VALUE_MIN_NUM){
                throw new SystemException(AppHttpCodeEnum.VALUE_LITTLE_MIN_NUM);
            }
        }

        List<Long> ids = ListUtil.toList(id);
        boolean remove = removeByIds(ids);
        if (!remove){
            throw new SystemException(DELETE_UNSUCCESS);
        }

        return ResponseResult.okResult(SUCCESS);
    }

    @Override
    public ResponseResult updateTag(UpdateTagDto updateTagDto) {
        if (ObjUtil.isEmpty(updateTagDto)){
            throw new SystemException(SYSTEM_ERROR);
        }

        Tag tag = BeanUtil.toBean(updateTagDto, Tag.class);
        boolean update = updateById(tag);
        if (!update){
            throw new SystemException(UPDATE_UNSUCCESS);
        }

        return ResponseResult.okResult(SUCCESS);
    }

    @Override
    public ResponseResult listAllTag() {
        List<Tag> tagList = lambdaQuery().list();

        if (tagList.isEmpty()){
            return ResponseResult.okResult(SUCCESS);
        }

        List<TagVO> voList = BeanUtil.copyToList(tagList, TagVO.class);

        return ResponseResult.okResult(voList);
    }
}
