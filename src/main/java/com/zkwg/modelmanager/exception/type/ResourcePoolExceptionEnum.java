package com.zkwg.modelmanager.exception.type;

import com.zkwg.modelmanager.exception.BusinessExceptionAssert;

public enum ResourcePoolExceptionEnum implements BusinessExceptionAssert {

    Resource_Pool_Not_Running(9050, "资源池没有运行"),

    Resource_Pool_Is_Running(9051, "资源池正在运行，不能修改");

    private int code;

    private String message;

    ResourcePoolExceptionEnum(int i, String s) {
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
