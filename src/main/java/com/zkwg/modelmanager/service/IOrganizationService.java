package com.zkwg.modelmanager.service;

import com.zkwg.modelmanager.entity.Organization;
import com.zkwg.modelmanager.service.base.IBaseService;

import java.util.List;

public interface IOrganizationService extends IBaseService<Organization> {

    List<Organization> selectOrgTree(Integer userId);
}
