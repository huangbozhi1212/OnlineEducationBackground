package com.atguigu.statisticsservice.service;

import com.atguigu.statisticsservice.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-05-22
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    void createStatisticsByDate(String day);

    Map<String, Object> getCharDate(String begin, String end, String type);
}
