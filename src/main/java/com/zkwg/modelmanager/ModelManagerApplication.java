package com.zkwg.modelmanager;

import com.zkwg.modelmanager.utils.SpringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;


@SpringBootApplication
public class ModelManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModelManagerApplication.class, args);
    }


    /**
     * Spring 工具类
     */
    @Bean
    public SpringUtils getSpringUtils(ApplicationContext applicationContext) {
        SpringUtils.setApplicationContext(applicationContext);
        return new SpringUtils();
    }

}
