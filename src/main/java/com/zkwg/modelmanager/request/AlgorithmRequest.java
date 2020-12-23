package com.zkwg.modelmanager.request;


public class AlgorithmRequest extends PageInfoRequest {

    private String creator;

    private String name;

    private Byte algTypeId;

    private String version;

    private String pictrue;

    private String remark;

    public AlgorithmRequest() {
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getAlgTypeId() {
        return algTypeId;
    }

    public void setAlgTypeId(Byte algTypeId) {
        this.algTypeId = algTypeId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPictrue() {
        return pictrue;
    }

    public void setPictrue(String pictrue) {
        this.pictrue = pictrue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
