package com.zkwg.modelmanager.entity;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2020-09-22
 */
@Data
public class ResourceOrg  {

    private static final long serialVersionUID = 1L;

    private Integer resourceId;

    private Integer orgId;

    /**
     * 1：算法 2： 模型 3：服务
     */
    private Integer resourceType;


}
