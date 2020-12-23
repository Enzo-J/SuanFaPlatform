package com.zkwg.modelmanager.core;

import com.zkwg.modelmanager.entity.Server;
import com.zkwg.modelmanager.entity.seldon.SeldonDeployment;
import com.zkwg.modelmanager.ws.WebSocketServer;

public class K8sDeployResult {

    private Integer tenantId;

    private String userId;

    private Server server;

    private WebSocketServer webSocketServer;

    private Status status;

    private String deployResultStr;

    public K8sDeployResult(){}

    public K8sDeployResult(Integer tenantId,Status status, String deployResultStr) {
        this.tenantId = tenantId;
        this.status = status;
        this.deployResultStr = deployResultStr;
    }

    public K8sDeployResult(Integer tenantId,Status status, String deployResultStr, Server server, WebSocketServer webSocketServer, String userId ) {
        this.tenantId = tenantId;
        this.status = status;
        this.deployResultStr = deployResultStr;
        this.server =  server;
        this.webSocketServer = webSocketServer;
        this.userId = userId;
    }

//    public K8sDeployResult(Status status, String deployResultStr) {
//        this.status = status;
//        this.deployResultStr = deployResultStr;
//    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Status getStatus() {
        return status;
    }

    public String getDeployResultStr() {
        return deployResultStr;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public WebSocketServer getWebSocketServer() {
        return webSocketServer;
    }

    public void setWebSocketServer(WebSocketServer webSocketServer) {
        this.webSocketServer = webSocketServer;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static enum Status {
        RUNNING,
        STOP,
        TIME_OUT,
        ERROR;
        private Status() {
        }
    }
}
