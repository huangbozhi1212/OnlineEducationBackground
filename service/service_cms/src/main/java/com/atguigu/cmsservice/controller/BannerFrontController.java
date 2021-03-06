package com.atguigu.cmsservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.cmsservice.entity.CrmBanner;
import com.atguigu.cmsservice.service.CrmBannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-05-12
 */
@RestController
@RequestMapping("/cmsservice/banner")
@Api("网站首页Banner列表")
@CrossOrigin //跨域
public class BannerFrontController {
    @Autowired
    private CrmBannerService bannerService;
    @ApiOperation(value = "获取首页banner")
    @GetMapping("getAllBanner")
    public R index() {
        List<CrmBanner> list = bannerService.selectIndexList();
        return R.ok().data("bannerList", list);
    }

}

