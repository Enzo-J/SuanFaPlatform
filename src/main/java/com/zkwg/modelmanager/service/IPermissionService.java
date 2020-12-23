package com.zkwg.modelmanager.service;

import com.zkwg.modelmanager.entity.Algorithm;
import com.zkwg.modelmanager.entity.Permission;
import com.zkwg.modelmanager.service.base.IBaseService;

import java.util.Arrays;
import java.util.List;

public interface IPermissionService extends IBaseService<Permission> {

    List<String> findPermsByUserId(Integer userId);

    List<Permission> selectMenuTree(Integer userId);
}
