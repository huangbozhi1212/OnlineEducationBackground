package com.atguigu.statisticsservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.statisticsservice.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-05-22
 */
@RestController
@RequestMapping("/staservice/statistics")
public class StatisticsDailyController {
    @Autowired
    private StatisticsDailyService statisticsDailyService;

    @PostMapping("registerCount/{day}")
    public R createStatisticsByDate(@PathVariable String day){
        statisticsDailyService.createStatisticsByDate(day);
        return R.ok();
    }
    @GetMapping("showChart/{begin}/{end}/{type}")
    public R showChart(@PathVariable String begin,@PathVariable String end,@PathVariable String type){
        Map<String,Object> map = statisticsDailyService.getCharDate(begin,end,type);
        return R.ok().data(map);
    }
}



