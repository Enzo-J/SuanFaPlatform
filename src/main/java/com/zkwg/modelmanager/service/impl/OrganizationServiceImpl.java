package com.zkwg.modelmanager.service.impl;

import com.zkwg.modelmanager.entity.Organization;
import com.zkwg.modelmanager.entity.Permission;
import com.zkwg.modelmanager.entity.Role;
import com.zkwg.modelmanager.mapper.OrganizationMapper;
import com.zkwg.modelmanager.mapper.RoleMapper;
import com.zkwg.modelmanager.service.IOrganizationService;
import com.zkwg.modelmanager.service.IRoleService;
import com.zkwg.modelmanager.service.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizationServiceImpl extends BaseService<OrganizationMapper, Organization> implements IOrganizationService {

    private static Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    private OrganizationMapper organizationMapper;

    @Autowired
    public void setModelMapper(OrganizationMapper organizationMapper) {
        this.organizationMapper = organizationMapper;
        this.baseMapper = organizationMapper;
    }

    @Override
    public List<Organization> selectOrgTree(Integer userId) {
        List<Organization> organizations = organizationMapper.selectList(null);
//        Assert.notEmpty(permissions, "");
        List<Organization> filterOrganizations =  organizations.stream().filter(o -> o.getIsDelete() == 0).collect(Collectors.toList());
        List<Organization> root = filterOrganizations.stream().filter(p -> p.getParentId() == 0).collect(Collectors.toList());
        setChildren(root, filterOrganizations);
        return root;
    }

    private void setChildren(List<Organization> root, List<Organization> resource) {
        for(Organization organization : root) {
            List<Organization> children = resource.stream().filter(p -> p.getParentId() == organization.getId()).collect(Collectors.toList());
            if(ObjectUtils.isEmpty(children)) { continue; }
            organization.setChildren(children);
            setChildren(children, resource);
        }
    }
}
