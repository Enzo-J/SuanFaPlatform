package com.zkwg.modelmanager.exception.type;

import com.zkwg.modelmanager.exception.BusinessExceptionAssert;
import com.zkwg.modelmanager.response.BaseResponse;

public enum ArgumentResponseEnum implements BusinessExceptionAssert {

    VALID_ERROR(9001, "Bad licence type.") ;

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回消息
     */
    private String message;

    ArgumentResponseEnum(int i, String s) {
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
