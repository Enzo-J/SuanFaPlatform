package com.zkwg.modelmanager.core;

public class JobRunResultMessage implements IMessage<JobRunResult> {

    private JobRunResult jobRunResult;

    public JobRunResultMessage(JobRunResult jobRunResult) {
        this.jobRunResult = jobRunResult;
    }

    @Override
    public JobRunResult getMessage() {
        return this.jobRunResult;
    }

}
