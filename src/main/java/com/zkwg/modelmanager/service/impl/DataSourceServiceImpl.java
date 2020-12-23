package com.zkwg.modelmanager.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zkwg.modelmanager.entity.DatabaseTypeEnum;
import com.zkwg.modelmanager.entity.Datasource;
import com.zkwg.modelmanager.mapper.DatasourceMapper;
import com.zkwg.modelmanager.service.IDataSourceService;
import com.zkwg.modelmanager.service.base.BaseService;
import com.zkwg.modelmanager.utils.DolphinSchedulerClientUtils;
import net.sf.jsqlparser.schema.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class DataSourceServiceImpl extends BaseService<DatasourceMapper, Datasource> implements IDataSourceService {

    private static Logger logger = LoggerFactory.getLogger(DataSourceServiceImpl.class);

    private DatasourceMapper datasourceMapper;

    @Autowired
    public void setModelMapper(DatasourceMapper datasourceMapper) {
        this.datasourceMapper = datasourceMapper;
        this.baseMapper = datasourceMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Datasource datasource) throws IOException {

        Map<String,String> params = new HashMap<>();
        params.put("name", datasource.getName());
        params.put("type", DatabaseTypeEnum.value(datasource.getType()));
        params.put("host",datasource.getHost());
        params.put("port", datasource.getPort()+"");
        params.put("database", datasource.getDatabase());
        params.put("userName", datasource.getUsername());
        params.put("password", datasource.getPassword());
        params.put("principal","");
        params.put("note","");
        params.put("other","");
        params.put("connectType","");

        DolphinSchedulerClientUtils.createDatasources(params);

        int id = DolphinSchedulerClientUtils.queryDataSourceListPaging(datasource.getName());

        datasource.setId(id);

        datasourceMapper.insert(datasource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Datasource datasource) throws IOException {
        DolphinSchedulerClientUtils.deleteDatasources(datasource.getId());
        datasourceMapper.delete(Wrappers.lambdaUpdate(datasource));
    }

}
