package com.zkwg.modelmanager.entity;

public enum DatabaseTypeEnum {

    MYSQL(1,"MYSQL");

    private Integer code;

    private String type;

    DatabaseTypeEnum(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    public static String value(Integer code) {
        for(DatabaseTypeEnum databaseTypeEnum : DatabaseTypeEnum.values()) {
            if(code == databaseTypeEnum.code) {
                return databaseTypeEnum.type;
            }
        }
        return "";
    }
}
