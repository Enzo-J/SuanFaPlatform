package com.zkwg.modelmanager.response;

import com.zkwg.modelmanager.exception.IResponseEnum;
import org.apache.poi.ss.formula.functions.T;

public class CommonResponse<T> extends BaseResponse {

    public T data;

    public CommonResponse() {
    }

    public CommonResponse(T data) {
        this.data = data;
    }

    public CommonResponse(int code, String message, T data) {
        super(code, message);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
