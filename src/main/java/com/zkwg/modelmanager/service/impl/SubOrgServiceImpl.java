package com.zkwg.modelmanager.service.impl;

import com.zkwg.modelmanager.entity.Permission;
import com.zkwg.modelmanager.entity.SubOrg;
import com.zkwg.modelmanager.mapper.PermissionMapper;
import com.zkwg.modelmanager.mapper.SubOrgMapper;
import com.zkwg.modelmanager.service.IPermissionService;
import com.zkwg.modelmanager.service.ISubOrgService;
import com.zkwg.modelmanager.service.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubOrgServiceImpl extends BaseService<SubOrgMapper, SubOrg> implements ISubOrgService {

    private static Logger logger = LoggerFactory.getLogger(SubOrgServiceImpl.class);

    private SubOrgMapper subOrgMapper;

    @Autowired
    public void setModelMapper(SubOrgMapper subOrgMapper) {
        this.subOrgMapper = subOrgMapper;
        this.baseMapper = subOrgMapper;
    }

}
