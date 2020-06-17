package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VodFileDegradeFeignClient implements VodClient{
    //出错后会执行
    @Override
    public R removelAlyVideo(String id) {
        return R.error().message("删除视频失败，请稍后再试");
    }

    @Override
    public R removeVideoList(List<String> videoIdList) {
        return R.error().message("删除视频失败，请稍后再试");
    }
}
