package com.atguigu.cmsservice.controller;


import com.atguigu.cmsservice.client.OssClient;
import com.atguigu.commonutils.R;
import com.atguigu.cmsservice.entity.CrmBanner;
import com.atguigu.cmsservice.service.CrmBannerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/cmsservice/bannerAdmin")
@Api("网站首页Banner列表")
//@CrossOrigin //跨域
public class BannerAdminController {
    @Autowired
    private CrmBannerService bannerService;

    @Autowired
    private OssClient ossClient;

    @ApiOperation(value = "获取Banner分页列表")
    @GetMapping("{page}/{limit}")
    public R index(
    @ApiParam(name = "page", value = "当前页码", required = true)
        @PathVariable Long page,
    @ApiParam(name = "limit", value = "每页记录数", required = true)
        @PathVariable Long limit) {
        Page<CrmBanner> pageParm = new Page<>(page, limit);
        QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        bannerService.page(pageParm,wrapper);
        List<CrmBanner> records = pageParm.getRecords(); //当前页数据
        long total = pageParm.getTotal(); //总记录数
        return R.ok().data("items", records).data("total", total);
    }
    @ApiOperation(value = "获取Banner")
    @GetMapping("get/{id}")
    public R get(@PathVariable String id) {
        CrmBanner banner = bannerService.getById(id);
        return R.ok().data("item", banner);
    }
    @ApiOperation(value = "新增Banner")
    @PostMapping("save")
    public R save(@RequestBody CrmBanner banner) {
        bannerService.save(banner);
        return R.ok();
    }
    @ApiOperation(value = "修改Banner")
    @PutMapping("update")
    public R updateById(@RequestBody CrmBanner banner) {
        bannerService.updateById(banner);
        return R.ok();
    }
    @ApiOperation(value = "删除Banner")
    @DeleteMapping("remove/{id}")
    public R remove(@PathVariable String id) {
        bannerService.removeById(id);
        return R.ok();
    }
}

