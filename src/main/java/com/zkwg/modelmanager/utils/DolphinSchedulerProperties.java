package com.zkwg.modelmanager.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


@Component
@Data
public class DolphinSchedulerProperties {

    @Value("${dolphinscheduler.url}")
    private String baseUrl;

    @Value("${dolphinscheduler.project-name}")
    private String projectName;

    @Value("${dolphinscheduler.token}")
    private String token;

}