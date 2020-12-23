package com.zkwg.modelmanager.service.impl;

import com.zkwg.modelmanager.entity.Algorithm;
import com.zkwg.modelmanager.mapper.AlgorithmMapper;
import com.zkwg.modelmanager.service.IAlgorithmService;
import com.zkwg.modelmanager.service.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlgorithmServiceImpl extends BaseService<AlgorithmMapper, Algorithm> implements IAlgorithmService {

    private static Logger logger = LoggerFactory.getLogger(AlgorithmServiceImpl.class);

    private AlgorithmMapper algorithmMapper;

    @Autowired
    public void setModelMapper(AlgorithmMapper algorithmMapper) {
        this.algorithmMapper = algorithmMapper;
        this.baseMapper = algorithmMapper;
    }
}
