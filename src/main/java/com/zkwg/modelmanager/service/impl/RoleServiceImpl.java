package com.zkwg.modelmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkwg.modelmanager.entity.*;
import com.zkwg.modelmanager.mapper.RoleMapper;
import com.zkwg.modelmanager.mapper.RolePermissionMapper;
import com.zkwg.modelmanager.request.RoleRequest;
import com.zkwg.modelmanager.security.dataScope.DataScope;
import com.zkwg.modelmanager.service.IRoleOrgService;
import com.zkwg.modelmanager.service.IRolePermissionService;
import com.zkwg.modelmanager.service.IRoleService;
import com.zkwg.modelmanager.service.base.BaseService;
import com.zkwg.modelmanager.service.strategy.DataScopeContext;
import com.zkwg.modelmanager.utils.SecurityUtil;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends BaseService<RoleMapper, Role> implements IRoleService {

    private static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    private RoleMapper roleMapper;

    @Autowired
    private IRolePermissionService rolePermissionService;

    @Autowired
    private IRoleOrgService roleOrgService;

    @Autowired
    private DataScopeContext dataScopeContext;

    @Autowired
    public void setModelMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
        this.baseMapper = roleMapper;
    }

    @Override
    public Set<String> selectUserRoleListByUserId(int userId) {
        return roleMapper.selectUserRoleListByUserId(userId)
                .stream()
                .map(sysUserRole -> "ROLE_" + sysUserRole.getId())
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional( rollbackFor = Exception.class)
    public void createRole(RoleRequest roleRequest) {
        // 插入角色信息
        Role role = new Role();
        BeanUtils.copyProperties(roleRequest,role);
        role.setStatus((byte) 1);
        role.setIsDelete((byte) 0);
        role.setCreateTime(new Date());
        role.setCreator(SecurityUtil.getUser().getUserId());
        roleMapper.insert(role);
        // 插入角色组织信息
//        saveRoleOrg(role, roleRequest.getOrgList());
        // 插入角色权限信息
        List<Integer> permissionIds = roleRequest.getPermissionIds();
        List<RolePermission> rolePermissions = permissionIds.stream().map(permissionId -> new RolePermission(role.getId(), permissionId)).collect(Collectors.toList());
        rolePermissionService.saveBatch(rolePermissions);
    }

    private void saveRoleOrg(Role role, List<Integer> orgList) {
        Integer userId = SecurityUtil.getUser().getUserId();
        // 根据 数据范围类型 和 勾选的组织ID， 重新计算全量的组织ID
        List<Integer> orgIds = dataScopeContext.getOrgIdsForDataScope(orgList, role.getDsType(), userId);
        if (orgIds != null && !orgIds.isEmpty()) {
            List<RoleOrg> list = orgIds.stream().map((orgId) -> new RoleOrg(role.getId(), orgId)).collect(Collectors.toList());
            roleOrgService.saveBatch(list);
        }
    }

    @Override
    @Transactional( rollbackFor = Exception.class)
    public void updateRole(RoleRequest roleRequest) {
        // 插入角色信息
        Role role = new Role();
        BeanUtils.copyProperties(roleRequest,role);
        //role.setStatus((byte) 1);
        role.setUpdateTime(new Date());
        roleMapper.updateById(role);
        //
        roleOrgService.remove(new QueryWrapper<>(new RoleOrg(role.getId(), null)));
        rolePermissionService.remove(new QueryWrapper<>(new RolePermission(role.getId(), null)));
        //saveRoleOrg(role, roleRequest.getOrgList());
        // 插入角色权限信息
        List<Integer> permissionIds = roleRequest.getPermissionIds();
        List<RolePermission> rolePermissions = permissionIds.stream().map(permissionId -> new RolePermission(role.getId(), permissionId)).collect(Collectors.toList());
        rolePermissionService.saveBatch(rolePermissions);
    }

    @Override
    public IPage<Role> getRolesPage(Integer pageNum, Integer pageSize, RoleRequest roleRequest) {
        return roleMapper.getRolesPage(new Page<T>(pageNum, pageSize) , roleRequest, new DataScope());
    }
}
