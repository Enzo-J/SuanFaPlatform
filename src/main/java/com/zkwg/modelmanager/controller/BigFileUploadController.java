package com.zkwg.modelmanager.controller;

import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 大文件上传
 */
@RestController
@RequestMapping("/BigFile")
@CrossOrigin
public class BigFileUploadController {
    @Autowired
    private FileService fileService;

    @PostMapping("/")
    public R upload(String name,
                    String md5,
                    Long size,
                    Integer chunks,
                    Integer chunk,
                    MultipartFile file) throws IOException {
        if (chunks != null && chunks != 0) {
            return ResultUtil.success(fileService.uploadWithBlock(name, md5,size,chunks,chunk,file));
        } else {
            return ResultUtil.success(fileService.upload(name, md5,file));
        }
    }
}
