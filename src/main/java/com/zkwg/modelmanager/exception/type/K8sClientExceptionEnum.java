package com.zkwg.modelmanager.exception.type;

import com.zkwg.modelmanager.exception.BusinessExceptionAssert;

public enum K8sClientExceptionEnum implements BusinessExceptionAssert {

    K8s_Connnection_Break(8000, "K8s 连接中断");

    private int code;

    private String message;

    K8sClientExceptionEnum(int i, String s) {
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
