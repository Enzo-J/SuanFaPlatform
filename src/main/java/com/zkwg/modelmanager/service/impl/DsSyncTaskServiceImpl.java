package com.zkwg.modelmanager.service.impl;


import com.zkwg.modelmanager.entity.DsSyncTask;
import com.zkwg.modelmanager.mapper.DsSyncTaskMapper;
import com.zkwg.modelmanager.service.IDsSyncTaskService;
import com.zkwg.modelmanager.service.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DsSyncTaskServiceImpl extends BaseService<DsSyncTaskMapper, DsSyncTask> implements IDsSyncTaskService {

    private static Logger logger = LoggerFactory.getLogger(DsSyncTaskServiceImpl.class);

    private DsSyncTaskMapper dsSyncTaskMapper;

    @Autowired
    public void setModelMapper(DsSyncTaskMapper dsSyncTaskMapper) {
        this.dsSyncTaskMapper = dsSyncTaskMapper;
        this.baseMapper = dsSyncTaskMapper;
    }
}
