package com.zkwg.modelmanager.core;

import com.zkwg.modelmanager.entity.Server;
import com.zkwg.modelmanager.entity.Training;
import com.zkwg.modelmanager.entity.seldon.SeldonDeployment;
import com.zkwg.modelmanager.ws.WebSocketServer;

public class JobRunResult {

    private String jobJsonMd5;

    private String runningPodJson;

    private Status status;

    public JobRunResult(){}

    public JobRunResult(Status status, SeldonDeployment seldonDeployment) {
        this.status = status;
    }

    public JobRunResult(Status status, SeldonDeployment seldonDeployment, Server server, WebSocketServer webSocketServer, String userId ) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getJobJsonMd5() {
        return jobJsonMd5;
    }

    public void setJobJsonMd5(String jobJsonMd5) {
        this.jobJsonMd5 = jobJsonMd5;
    }

    public String getRunningPodJson() {
        return runningPodJson;
    }

    public void setRunningPodJson(String runningPodJson) {
        this.runningPodJson = runningPodJson;
    }

    public static enum Status {
        PENDING,
        RUNNING,
        STOP,
        TIME_OUT,
        ERROR,
        DELETE,
        SUCCESS;
        private Status() {
        }
    }
}
