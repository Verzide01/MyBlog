package com.piggod.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.piggod.common.constants.SystemConstants;
import com.piggod.common.domain.po.Link;
import com.piggod.common.domain.vo.LinkVO;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.enums.AppHttpCodeEnum;
import com.piggod.common.mapper.LinkMapper;
import com.piggod.common.service.ILinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 友链 服务实现类
 * </p>
 *
 * @author 梁峰
 * @since 2025-03-17
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements ILinkService {

    @Override
    public ResponseResult getAllLink() {
        // 1.查询所有友链
        List<Link> list = lambdaQuery()
                .eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL)
                .list();
        if(list == null || list.isEmpty()){
            return ResponseResult.okResult();
        }

        // 2.封装数据返回
        List<LinkVO> voList = BeanUtil.copyToList(list, LinkVO.class);
        return ResponseResult.okResult(voList);
    }
}
