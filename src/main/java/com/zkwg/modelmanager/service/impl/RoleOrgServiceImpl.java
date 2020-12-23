package com.zkwg.modelmanager.service.impl;

import com.zkwg.modelmanager.entity.RoleOrg;
import com.zkwg.modelmanager.entity.RolePermission;
import com.zkwg.modelmanager.mapper.RoleOrgMapper;
import com.zkwg.modelmanager.mapper.RolePermissionMapper;
import com.zkwg.modelmanager.service.IRoleOrgService;
import com.zkwg.modelmanager.service.IRolePermissionService;
import com.zkwg.modelmanager.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2020-09-20
 */
@Service
public class RoleOrgServiceImpl extends BaseService<RoleOrgMapper, RoleOrg> implements IRoleOrgService {

    private RoleOrgMapper orgMapper;

    @Autowired
    public void setModelMapper(RoleOrgMapper orgMapper) {
        this.orgMapper = orgMapper;
        this.baseMapper = orgMapper;
    }
}
