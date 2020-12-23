package com.zkwg.modelmanager.service;

import com.zkwg.modelmanager.entity.DeployProcessDef;
import com.zkwg.modelmanager.entity.Model;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.service.base.IBaseService;

public interface IDeployProcessDefService extends IBaseService<DeployProcessDef> {

    R run(int id) throws Exception;

    void insertAndGetId(DeployProcessDef deployProcessDef);
}
