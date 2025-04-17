package com.piggod.common.service;

import com.piggod.common.domain.dto.AddLinkDTO;
import com.piggod.common.domain.dto.UpdateLinkDTO;
import com.piggod.common.domain.po.Link;
import com.baomidou.mybatisplus.extension.service.IService;
import com.piggod.common.domain.query.LinkPageQuery;
import com.piggod.common.domain.vo.ResponseResult;

/**
 * <p>
 * 友链 服务类
 * </p>
 *
 * @author 梁峰
 * @since 2025-03-17
 */
public interface ILinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult listLinkByPage(LinkPageQuery query);

    ResponseResult addLink(AddLinkDTO addLinkDTO);

    ResponseResult getLinkById(Long id);

    ResponseResult deleteLinkById(Long[] id);

    ResponseResult updateLink(UpdateLinkDTO updateLinkDTO);
}
