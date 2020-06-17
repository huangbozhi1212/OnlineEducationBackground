package com.atguigu.vod.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface VideoService {
    String uploadVideo(MultipartFile file);

    void removeVideo(String id);

    void removeVideoList(List<String> videoList);
}
