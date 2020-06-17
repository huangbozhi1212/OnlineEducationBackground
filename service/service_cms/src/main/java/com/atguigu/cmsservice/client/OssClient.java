package com.atguigu.cmsservice.client;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "service-oss")
@Component
public interface OssClient {
    //上传头像的方法
    @PostMapping("/eduoss/fileoss")
    R uploadOssFile(MultipartFile file);
}
