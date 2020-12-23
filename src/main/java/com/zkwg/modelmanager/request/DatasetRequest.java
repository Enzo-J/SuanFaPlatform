package com.zkwg.modelmanager.request;

import lombok.Data;

@Data
public class DatasetRequest extends PageInfoRequest {

    private String creator;

    private String name;

    private Integer type;
    /**
     * 数据来源 1：数据库 2：文件系统
     */
    private Integer comeFrom;

    /**
     * 源数据数据源uuid
     */
    private String source;

    /**
     * 目标数据源uuid
     */
    private String target;

    /**
     * 备注
     */
    private String remark;

    private String processDefinitionJson;

    private String locations;
}
