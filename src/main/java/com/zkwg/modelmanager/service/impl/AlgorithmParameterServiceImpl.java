package com.zkwg.modelmanager.service.impl;

import com.zkwg.modelmanager.entity.Algorithm;
import com.zkwg.modelmanager.entity.AlgorithmParameter;
import com.zkwg.modelmanager.mapper.AlgorithmMapper;
import com.zkwg.modelmanager.mapper.AlgorithmParameterMapper;
import com.zkwg.modelmanager.service.IAlgorithmParameterService;
import com.zkwg.modelmanager.service.IAlgorithmService;
import com.zkwg.modelmanager.service.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlgorithmParameterServiceImpl extends BaseService<AlgorithmParameterMapper, AlgorithmParameter> implements IAlgorithmParameterService {

    private static Logger logger = LoggerFactory.getLogger(AlgorithmParameterServiceImpl.class);

    private AlgorithmParameterMapper algorithmParameterMapper;

    @Autowired
    public void setModelMapper(AlgorithmParameterMapper algorithmParameterMapper) {
        this.algorithmParameterMapper = algorithmParameterMapper;
        this.baseMapper = algorithmParameterMapper;
    }
}
