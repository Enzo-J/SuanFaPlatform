package com.zkwg.modelmanager.service.impl;

import com.zkwg.modelmanager.entity.Role;
import com.zkwg.modelmanager.entity.UserRole;
import com.zkwg.modelmanager.mapper.RoleMapper;
import com.zkwg.modelmanager.mapper.UserRoleMapper;
import com.zkwg.modelmanager.service.IRoleService;
import com.zkwg.modelmanager.service.IUserRoleService;
import com.zkwg.modelmanager.service.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl extends BaseService<UserRoleMapper, UserRole> implements IUserRoleService {

    private static Logger logger = LoggerFactory.getLogger(UserRoleServiceImpl.class);

    private UserRoleMapper userRoleMapper;

    @Autowired
    public void setModelMapper(UserRoleMapper userRoleMapper) {
        this.userRoleMapper = userRoleMapper;
        this.baseMapper = userRoleMapper;
    }

}
