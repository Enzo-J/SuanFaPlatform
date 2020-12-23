package com.zkwg.modelmanager.exception.type;

import com.zkwg.modelmanager.exception.BusinessExceptionAssert;

public enum MinioClientExceptionEnum implements BusinessExceptionAssert {

    Minio_Put_Object_Error(7000, "minio client 保存文件错误"),

    Minio_Url_Pattern_Error(7001, "minio url 格式错误")
    ;

    private int code;

    private String message;

    MinioClientExceptionEnum(int i, String s) {
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
