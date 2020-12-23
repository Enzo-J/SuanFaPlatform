package com.zkwg.modelmanager.request;

import lombok.Data;

@Data
public class ServerRequest extends PageInfoRequest {

    public interface Save{}

    private Integer userId;

    private String name;

    private String type;

    private String implementation;

    private Integer processDefId;

    private String models;

    private String containers;

    private String serverJson;

    private Integer businessType;
}
