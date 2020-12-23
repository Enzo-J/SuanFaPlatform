package com.zkwg.modelmanager.exception.type;

import com.zkwg.modelmanager.exception.BusinessExceptionAssert;

public enum DeployProcessDefExceptionEnum implements BusinessExceptionAssert {

    DeployProcessDef_Not_Exsist(9090, "部署流程定义不存在");

    private int code;

    private String message;

    DeployProcessDefExceptionEnum(int i, String s) {
        this.code = i;
        this.message = s;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
