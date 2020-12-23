package com.zkwg.modelmanager.service.impl;

import com.zkwg.modelmanager.entity.ResourcePool;
import com.zkwg.modelmanager.mapper.ResourcePoolMapper;
import com.zkwg.modelmanager.service.IResourcePoolService;
import com.zkwg.modelmanager.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourcePoolServiceImpl extends BaseService<ResourcePoolMapper, ResourcePool> implements IResourcePoolService {

    private ResourcePoolMapper resourcePoolMapper;

    @Autowired
    public void setResourcePoolMapper(ResourcePoolMapper resourcePoolMapper) {
        this.resourcePoolMapper = resourcePoolMapper;
        this.baseMapper = resourcePoolMapper;
    }


}
