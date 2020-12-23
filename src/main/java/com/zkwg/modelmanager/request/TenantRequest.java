package com.zkwg.modelmanager.request;


import lombok.Data;

import java.util.Date;
@Data
public class TenantRequest extends PageInfoRequest {

    private String tenantCode;

    private String tenantName;

    private Date endTime;

    public TenantRequest() {
    }
}
