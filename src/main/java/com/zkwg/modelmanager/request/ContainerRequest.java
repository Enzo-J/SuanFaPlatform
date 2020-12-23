package com.zkwg.modelmanager.request;

public class ContainerRequest extends PageInfoRequest {

    private Integer id;

    private String name;

    private String containerJson;

    private Byte status;

    private String creator;

    public ContainerRequest() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContainerJson() {
        return containerJson;
    }

    public void setContainerJson(String containerJson) {
        this.containerJson = containerJson;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
