package com.atguigu.statisticsservice.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.statisticsservice.client.UcenterClient;
import com.atguigu.statisticsservice.entity.StatisticsDaily;
import com.atguigu.statisticsservice.mapper.StatisticsDailyMapper;
import com.atguigu.statisticsservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-22
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public void createStatisticsByDate(String day) {
        //删除已存在的统计对象
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        baseMapper.delete(wrapper);
        //获取统计信息
        Integer registerNum = (Integer) ucenterClient.countRegister(day).getData().get("countRegister");
        Integer loginNum = RandomUtils.nextInt(100, 200);//TODO 每日登陆人数
        Integer videoViewNum = RandomUtils.nextInt(100, 200);//TODO 每日播放视频数
        Integer courseNum = RandomUtils.nextInt(100, 200);//TODO 每日新增课程数

        //创建统计对象
        StatisticsDaily statisticsDaily = new StatisticsDaily();
        statisticsDaily.setDateCalculated(day);//统计日期
        statisticsDaily.setRegisterNum(registerNum);//注册人数
        statisticsDaily.setLoginNum(loginNum);
        statisticsDaily.setCourseNum(courseNum);
        statisticsDaily.setVideoViewNum(videoViewNum);

        baseMapper.insert(statisticsDaily);
    }

    @Override
    public Map<String, Object> getCharDate(String begin, String end, String type) {
        System.out.println(type);
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.select(type,"date_calculated");
        wrapper.between("date_calculated",begin,end);

        List<StatisticsDaily> dailyList = baseMapper.selectList(wrapper);

        //因为返回有两部分数据：日期 和 日期对应数量
        //前端要求数组json结构，对应后端java代码是list集合
        //创建两个list集合，一个日期list，一个数量list
        List<String> date_calculatedList = new ArrayList<>();
        List<Integer> numDataList = new ArrayList<>();

        //遍历查询所有的数据list集合，进行封装
        for (int i = 0; i < dailyList.size(); i++) {
            StatisticsDaily daily = dailyList.get(i);
            //封装日期list集合
            date_calculatedList.add(daily.getDateCalculated());
            //封装对应数量
            switch (type) {
                case "login_num":
                    numDataList.add(daily.getLoginNum());
                    break;
                case "register_num":
                    numDataList.add(daily.getRegisterNum());
                    break;
                case "video_view_num":
                    numDataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    numDataList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }
        //把封装之后两个list集合放到map集合，进行返回
        Map<String, Object> map = new HashMap<>();
        map.put("date_calculatedList",date_calculatedList);
        map.put("numDataList",numDataList);
        return map;
    }
}
