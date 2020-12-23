package com.zkwg.modelmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.MultipartConfigElement;

/**
 * 开启WebSocket支持
 * @author zhengkai.blog.csdn.net
 */
@Configuration  
public class WebSocketConfig {  
	
    @Bean  
    public ServerEndpointExporter serverEndpointExporter() {  
        return new ServerEndpointExporter();  
    }

    /**
     * 配置文件上传大小
     */
//    @Bean
//    public MultipartConfigElement multipartConfigElement(){
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        //  单个数据大小 10M
//        factory.setMaxFileSize(DataSize.parse("10240KB"));
//        /// 总上传数据大小 10M
//        factory.setMaxRequestSize(DataSize.parse("10240KB"));
//        return factory.createMultipartConfig();
//    }
} 
