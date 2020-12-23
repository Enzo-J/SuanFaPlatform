package com.zkwg.modelmanager.service.impl;

import com.zkwg.modelmanager.entity.Algorithm;
import com.zkwg.modelmanager.entity.AlgorithmType;
import com.zkwg.modelmanager.mapper.AlgorithmMapper;
import com.zkwg.modelmanager.mapper.AlgorithmTypeMapper;
import com.zkwg.modelmanager.mapper.ContainerMapper;
import com.zkwg.modelmanager.service.IAlgorithmService;
import com.zkwg.modelmanager.service.IAlgorithmTypeService;
import com.zkwg.modelmanager.service.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlgorithmTypeServiceImpl extends BaseService<AlgorithmTypeMapper, AlgorithmType> implements IAlgorithmTypeService {

    private static Logger logger = LoggerFactory.getLogger(AlgorithmTypeServiceImpl.class);

    private AlgorithmTypeMapper algorithmTypeMapper;

    @Autowired
    public void setModelMapper(AlgorithmTypeMapper algorithmTypeMapper) {
        this.algorithmTypeMapper = algorithmTypeMapper;
        this.baseMapper = algorithmTypeMapper;
    }
}
