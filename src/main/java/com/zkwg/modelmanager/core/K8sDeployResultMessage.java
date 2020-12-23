package com.zkwg.modelmanager.core;

public class K8sDeployResultMessage implements IMessage<K8sDeployResult> {

    private K8sDeployResult deployResult;

    public K8sDeployResultMessage(K8sDeployResult deployResult) {
        this.deployResult = deployResult;
    }

    @Override
    public K8sDeployResult getMessage() {
        return this.deployResult;
    }

}
