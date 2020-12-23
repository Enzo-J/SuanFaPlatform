package com.zkwg.modelmanager.entity.seldon;

public class Parameter {

    private String name;

    private String value;

    private String type;

//    Name  string       `json:"name" protobuf:"string,1,opt,name=name"`
//    Value string       `json:"value" protobuf:"string,2,opt,name=value"`
//    Type  ParmeterType `json:"type" protobuf:"int,3,opt,name=type"`


    public Parameter() {
    }

    public Parameter(String name, String value, String type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
