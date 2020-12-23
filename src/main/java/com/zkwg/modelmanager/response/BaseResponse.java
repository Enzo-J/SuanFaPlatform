package com.zkwg.modelmanager.response;

public abstract class BaseResponse {

    public int code;

    public String message;

    public BaseResponse() {
    }

    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResultCode(ResultCode code) {
        this.code = code.getCode();
        this.message = code.getMessage();
    }
}
