package com.zkwg.modelmanager.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;


@Component
public class DockerProperties {

    @Value("${docker.url}")
    private String dockerUrl;

    @Value("${harbor.url}")
    private String harborUrl;

    @Value("${harbor.username}")
    private String username;

    @Value("${harbor.password}")
    private String password;

    @Value("${harbor.project}")
    private String project;


    public DockerProperties() {
    }

    public String getDockerUrl() {
        return dockerUrl;
    }

    public void setDockerUrl(String dockerUrl) {
        this.dockerUrl = dockerUrl;
    }

    public String getHarborUrl() {
        return harborUrl;
    }

    public void setHarborUrl(String harborUrl) {
        this.harborUrl = harborUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getRepositoryPath(String tag) {
        Assert.notNull(harborUrl,"harborUrl 不能为空！");
        Assert.notNull(project,"project 不能为空！");
        return harborUrl + "/" + project + "/" + tag;
    }
}