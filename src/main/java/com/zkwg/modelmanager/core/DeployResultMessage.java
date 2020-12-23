package com.zkwg.modelmanager.core;

public class DeployResultMessage implements IMessage<DeployResult> {

    private DeployResult deployResult;

    public DeployResultMessage(DeployResult deployResult) {
        this.deployResult = deployResult;
    }

    @Override
    public DeployResult getMessage() {
        return this.deployResult;
    }

}
