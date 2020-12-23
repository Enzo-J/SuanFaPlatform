package com.zkwg.modelmanager.service.impl;

import com.zkwg.modelmanager.entity.ResourceOrg;
import com.zkwg.modelmanager.mapper.ResourceOrgMapper;
import com.zkwg.modelmanager.service.IResourceOrgService;
import com.zkwg.modelmanager.service.base.BaseService;
import com.zkwg.modelmanager.service.strategy.DataScopeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ResourceOrgServiceImpl extends BaseService<ResourceOrgMapper, ResourceOrg> implements IResourceOrgService {

    private static Logger logger = LoggerFactory.getLogger(ResourceOrgServiceImpl.class);

    private ResourceOrgMapper resourceOrgMapper;


    @Autowired
    public void setModelMapper(ResourceOrgMapper resourceOrgMapper) {
        this.resourceOrgMapper = resourceOrgMapper;
        this.baseMapper = resourceOrgMapper;
    }
}
