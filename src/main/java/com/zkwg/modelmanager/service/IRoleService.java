package com.zkwg.modelmanager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zkwg.modelmanager.entity.Role;
import com.zkwg.modelmanager.request.RoleRequest;
import com.zkwg.modelmanager.service.base.IBaseService;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public interface IRoleService extends IBaseService<Role> {

    Set<String> selectUserRoleListByUserId(int userId);

    void createRole(RoleRequest roleRequest);

    void updateRole(RoleRequest roleRequest);

    IPage<Role> getRolesPage(Integer pageNum, Integer pageSize, RoleRequest roleRequest);
}
