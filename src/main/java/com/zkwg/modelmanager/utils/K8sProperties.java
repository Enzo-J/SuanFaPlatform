package com.zkwg.modelmanager.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;


@Component
//@ConfigurationProperties("k8s")
public class K8sProperties {

    @Value("${k8s.url}")
    private String url;

    @Value("${k8s.admin-conf}")
    private Resource adminConf;

    @Value("${k8s.client-crt}")
    private Resource clientCrt;

    @Value("${k8s.client-key}")
    private Resource clientKey;

    @Value("${k8s.ca-crt}")
    private Resource caCrt;

    public K8sProperties() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Resource getAdminConf() {
        return adminConf;
    }

    public void setAdminConf(Resource adminConf) {
        this.adminConf = adminConf;
    }

    public Resource getClientCrt() {
        return clientCrt;
    }

    public void setClientCrt(Resource clientCrt) {
        this.clientCrt = clientCrt;
    }

    public Resource getClientKey() {
        return clientKey;
    }

    public void setClientKey(Resource clientKey) {
        this.clientKey = clientKey;
    }

    public Resource getCaCrt() {
        return caCrt;
    }

    public void setCaCrt(Resource caCrt) {
        this.caCrt = caCrt;
    }
}