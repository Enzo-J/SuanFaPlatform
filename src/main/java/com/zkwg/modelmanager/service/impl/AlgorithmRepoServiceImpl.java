package com.zkwg.modelmanager.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkwg.modelmanager.entity.AlgorithmRepo;
import com.zkwg.modelmanager.mapper.AlgorithmRepoMapper;
import com.zkwg.modelmanager.request.AlgorithmRepoRequest;
import com.zkwg.modelmanager.security.dataScope.DataScope;
import com.zkwg.modelmanager.service.IAlgorithmRepoService;
import com.zkwg.modelmanager.service.base.BaseService;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlgorithmRepoServiceImpl extends BaseService<AlgorithmRepoMapper, AlgorithmRepo> implements IAlgorithmRepoService {

    private static Logger logger = LoggerFactory.getLogger(AlgorithmRepoServiceImpl.class);

    private AlgorithmRepoMapper algorithmRepoMapper;

    @Autowired
    public void setModelMapper(AlgorithmRepoMapper algorithmRepoMapper) {
        this.algorithmRepoMapper = algorithmRepoMapper;
        this.baseMapper = algorithmRepoMapper;
    }

    @Override
    public IPage<AlgorithmRepo> getAlgorithmRepoVosPage(int pageNum, int pageSize, AlgorithmRepoRequest algorithmRepoRequest) {
           return algorithmRepoMapper.getAlgorithmRepoVosPage(new Page<T>(pageNum, pageSize) , algorithmRepoRequest);
    }

}
