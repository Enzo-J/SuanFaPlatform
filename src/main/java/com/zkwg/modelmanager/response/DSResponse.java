package com.zkwg.modelmanager.response;

import lombok.Data;

@Data
public class DSResponse<T> {

    private Integer code;

    private String msg;

    private T data;

}
