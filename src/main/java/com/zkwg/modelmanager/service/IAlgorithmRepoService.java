package com.zkwg.modelmanager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zkwg.modelmanager.entity.AlgorithmRepo;
import com.zkwg.modelmanager.request.AlgorithmRepoRequest;
import com.zkwg.modelmanager.security.dataScope.DataScope;
import com.zkwg.modelmanager.service.base.IBaseService;

public interface IAlgorithmRepoService extends IBaseService<AlgorithmRepo> {

    IPage<AlgorithmRepo> getAlgorithmRepoVosPage(int pageNum, int pageSize, AlgorithmRepoRequest algorithmRepoRequest);

}
