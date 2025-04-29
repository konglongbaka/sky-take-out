package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")

public class CommonController {
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);
    @Autowired
    private AliOssUtil aliOssUtil;

    @RequestMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("上传文件开始");
        try {
            String originalFilename = file.getOriginalFilename();
            String extend = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = UUID.randomUUID() + extend;
            String url = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(url);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new RuntimeException(e);
        }

    }
}
