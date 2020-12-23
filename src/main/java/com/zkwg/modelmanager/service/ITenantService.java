package com.zkwg.modelmanager.service;

import com.zkwg.modelmanager.entity.Tenant;
import com.zkwg.modelmanager.service.base.IBaseService;

public interface ITenantService extends IBaseService<Tenant> {

    void init(Tenant tenant);
}
