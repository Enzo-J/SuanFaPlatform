package com.zkwg.modelmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zkwg.modelmanager.entity.Permission;
import com.zkwg.modelmanager.entity.UserRole;
import com.zkwg.modelmanager.mapper.PermissionMapper;
import com.zkwg.modelmanager.service.IPermissionService;
import com.zkwg.modelmanager.service.IRolePermissionService;
import com.zkwg.modelmanager.service.IUserRoleService;
import com.zkwg.modelmanager.service.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl extends BaseService<PermissionMapper, Permission> implements IPermissionService {

    private static Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRolePermissionService rolePermissionService;

    private PermissionMapper permissionMapper;

    @Autowired
    public void setModelMapper(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
        this.baseMapper = permissionMapper;
    }

    @Override
    public List<String> findPermsByUserId(Integer userId) {
        // 查询权限
        return permissionMapper.findPermsByUserId(userId);
    }

    @Override
    public List<Permission> selectMenuTree(Integer userId) {

        List<Permission> permissions = permissionMapper.selectPermisstionsByUserId(userId);
//        Assert.notEmpty(permissions, "");
        List<Permission> root = permissions.stream()
                                .filter(p -> p.getParentId() == 0)
                                .sorted(Comparator.comparing(Permission::getSort))
                                .collect(Collectors.toList());
        setChildren(root, permissions);
        return root;
    }

    private void setChildren(List<Permission> root, List<Permission> resource) {
        for(Permission permission : root) {
            List<Permission> children = resource.stream()
                                        .filter(p -> p.getParentId() == permission.getId())
                                        .sorted(Comparator.comparing(Permission::getSort))
                                        .collect(Collectors.toList());
            if(ObjectUtils.isEmpty(children)) { continue; }
            permission.setChildren(children);
            setChildren(children, resource);
        }
    }

}
