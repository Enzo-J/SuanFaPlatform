package com.zkwg.modelmanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UploadConfig {

    public static String path;

    @Value("${web.upload-path}")
    public void setPath(String path) {
        UploadConfig.path = path;
    }
}
