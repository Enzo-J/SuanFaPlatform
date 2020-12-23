package com.zkwg.modelmanager.response;

import org.apache.poi.ss.formula.functions.T;

public class R<T> extends CommonResponse<T> {

    public R(T data) {
        super(data);
    }

    public R(int code, String message, T data) {
        super(code, message, data);
    }

    public R() {
    }
}
