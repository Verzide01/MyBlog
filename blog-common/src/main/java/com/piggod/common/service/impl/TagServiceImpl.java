package com.piggod.common.service.impl;

import com.piggod.common.domain.po.Tag;
import com.piggod.common.mapper.TagMapper;
import com.piggod.common.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
