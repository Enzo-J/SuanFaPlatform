package com.zkwg.modelmanager.exception.type;

import com.zkwg.modelmanager.exception.BusinessExceptionAssert;

public enum ModelExceptionEnum implements BusinessExceptionAssert {

    Model_Not_Exsist(9080, "模型不存在");

    private int code;

    private String message;

    ModelExceptionEnum(int i, String s) {
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
