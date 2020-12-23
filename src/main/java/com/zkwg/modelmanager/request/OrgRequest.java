package com.zkwg.modelmanager.request;


public class OrgRequest extends PageInfoRequest {

    /**
     * 组织名称
     */
    private String name;

    /**
     * 组织编号
     */
    private String orgCode;

    /**
     * 组织类型（公司、政府单位）
     */
    private Byte type;

    private Integer parentId;

    public OrgRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
