package com.zkwg.modelmanager.exception.base;

import com.zkwg.modelmanager.exception.IResponseEnum;

public class BaseException extends RuntimeException {

    private int code;

    private String message;

    private IResponseEnum responseEnum;

    private Object[] args;

    public BaseException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(message,cause);
        this.message = message;
        this.responseEnum = responseEnum;
        this.args = args;
    }

    public BaseException(IResponseEnum responseEnum) {
        this.responseEnum = responseEnum;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public IResponseEnum getResponseEnum() {
        return responseEnum;
    }

    public void setResponseEnum(IResponseEnum responseEnum) {
        this.responseEnum = responseEnum;
    }
}
