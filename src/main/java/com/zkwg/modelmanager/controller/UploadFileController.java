package com.zkwg.modelmanager.controller;

import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class UploadFileController {

    private static Logger logger = LoggerFactory.getLogger(UploadFileController.class);

    @Value("${web.uploadpath}")
    private String uploadPath;


    @PostMapping("/{path}")
    @ResponseBody
    public R upload(@PathVariable String path, @RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResultUtil.failure(null,"上传失败，请选择文件");
        }

        String basePath = uploadPath + "static/picture/" + path;

        String destPath = FileUtils.transferTo(file,basePath);

        return ResultUtil.success(destPath.replace(uploadPath,""));
    }


}
