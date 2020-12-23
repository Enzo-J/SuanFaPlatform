package com.zkwg.modelmanager.core;

import com.zkwg.modelmanager.entity.Server;
import com.zkwg.modelmanager.entity.seldon.SeldonDeployment;
import com.zkwg.modelmanager.ws.WebSocketServer;

public class DeployWSResult extends DeployResult {

    private Server server;

    private WebSocketServer webSocketServer;

    public DeployWSResult(){}

    public DeployWSResult(Status status, SeldonDeployment seldonDeployment) {
        super(status,seldonDeployment);
    }

    public DeployWSResult(Status status, SeldonDeployment seldonDeployment, Server server, WebSocketServer webSocketServer) {
        super(status,seldonDeployment);
        this.server = server;
        this.webSocketServer = webSocketServer;
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
}
