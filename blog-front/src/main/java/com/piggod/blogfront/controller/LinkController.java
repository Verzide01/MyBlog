package com.piggod.blogfront.controller;


import com.piggod.common.annotation.SystemLog;
import com.piggod.common.domain.vo.ResponseResult;
import com.piggod.common.service.ILinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 友链 前端控制器
 * </p>
 *
 * @author 梁峰
 * @since 2025-03-17
 */
@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private ILinkService linkService;

    @SystemLog(bussinessName = "获取所有友链")
    @GetMapping("/getAllLink")
    public ResponseResult getAllLink() {
        return linkService.getAllLink();
    }

}
