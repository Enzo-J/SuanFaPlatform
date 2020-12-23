package com.zkwg.modelmanager.service.impl;

import com.zkwg.modelmanager.entity.Role;
import com.zkwg.modelmanager.entity.RolePermission;
import com.zkwg.modelmanager.mapper.RoleMapper;
import com.zkwg.modelmanager.mapper.RolePermissionMapper;
import com.zkwg.modelmanager.service.IRolePermissionService;
import com.zkwg.modelmanager.service.IRoleService;
import com.zkwg.modelmanager.service.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RolePermissionServiceImpl extends BaseService<RolePermissionMapper, RolePermission> implements IRolePermissionService {

    private static Logger logger = LoggerFactory.getLogger(RolePermissionServiceImpl.class);

    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    public void setModelMapper(RolePermissionMapper rolePermissionMapper) {
        this.rolePermissionMapper = rolePermissionMapper;
        this.baseMapper = rolePermissionMapper;
    }

}
