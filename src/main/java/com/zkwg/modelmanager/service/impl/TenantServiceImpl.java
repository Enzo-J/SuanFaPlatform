package com.zkwg.modelmanager.service.impl;

import com.zkwg.modelmanager.core.BaseContextHandler;
import com.zkwg.modelmanager.entity.*;
import com.zkwg.modelmanager.mapper.TenantMapper;
import com.zkwg.modelmanager.service.*;
import com.zkwg.modelmanager.service.base.BaseService;
import com.zkwg.modelmanager.utils.AIMenuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TenantServiceImpl extends BaseService<TenantMapper, Tenant> implements ITenantService {

    private static Logger logger = LoggerFactory.getLogger(TenantServiceImpl.class);

    private TenantMapper tenantMapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRolePermissionService rolePermissionService;

    @Autowired
    private IAlgorithmTypeService algorithmTypeService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IAlgorithmParameterService algorithmParameterService;

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    public void setModelMapper(TenantMapper tenantMapper) {
        this.tenantMapper = tenantMapper;
        this.baseMapper = tenantMapper;
    }

    /**
     * 初始化租户
     * @param tenant
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void init(Tenant tenant) {

        BaseContextHandler.setTenant(tenant.getId());

        // 查询所有权限
        List<Permission> permissions = permissionService.selectAll();
        // 去除 租户管理/运营用户
        List<Permission> newPermissions = permissions.stream().filter(p -> !(p.getId() == 81 || p.getId() == 82)).collect(Collectors.toList());

        // 创建系统管理员角色
        Role adminRole = AIMenuUtil.createAdminRole();
        roleService.insertSelective(adminRole);

        // 创建角色权限链接
        List<RolePermission> rolePermissions = newPermissions.stream().map(p -> new RolePermission(adminRole.getId(), p.getId())).collect(Collectors.toList());
        rolePermissionService.saveBatch(rolePermissions);

        // 创建admin用户
        User adminUser = AIMenuUtil.createAdminUser(tenant);
        userService.insertSelective(adminUser);

        // 创建用户角色链接
        userRoleService.insertSelective(new UserRole(adminUser.getId(), adminRole.getId()));

        // 修改租户状态
        tenant.setStatus((byte) 2);
        tenant.setUpdateTime(new Date());
        tenantMapper.updateById(tenant);
        // 初始化系统环境

        // 为主分配容器资源

        // 添加训练参数

//        algorithmTypeService.saveBatch();
        // 添加算法类型

//        algorithmParameterService.saveBatch()
        // 添加数据字典
    }

}
