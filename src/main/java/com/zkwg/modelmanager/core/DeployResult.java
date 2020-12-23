package com.zkwg.modelmanager.core;

import com.zkwg.modelmanager.entity.Server;
import com.zkwg.modelmanager.entity.seldon.SeldonDeployment;
import com.zkwg.modelmanager.ws.WebSocketServer;

public class DeployResult {

    private Integer tenantId;

    private String userId;

    private Server server;

    private WebSocketServer webSocketServer;

    private Status status;

    private SeldonDeployment seldonDeployment;

    public DeployResult(){}

    public DeployResult(Status status, SeldonDeployment seldonDeployment) {
        this.status = status;
        this.seldonDeployment = seldonDeployment;
    }

    public DeployResult(Integer tenantId, Status status, SeldonDeployment seldonDeployment, Server server, WebSocketServer webSocketServer, String userId ) {
        this.tenantId = tenantId;
        this.status = status;
        this.seldonDeployment = seldonDeployment;
        this.server =  server;
        this.webSocketServer = webSocketServer;
        this.userId = userId;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Status getStatus() {
        return status;
    }

    public SeldonDeployment getSeldonDeployment(){
        return seldonDeployment;
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
        SUCCESS,
        RUNNING,
        STOP,
        TIME_OUT,
        ERROR;
        private Status() {
        }
    }
}
