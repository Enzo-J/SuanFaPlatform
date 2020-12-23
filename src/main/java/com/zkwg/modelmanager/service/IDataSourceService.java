package com.zkwg.modelmanager.service;


import com.zkwg.modelmanager.entity.Datasource;
import com.zkwg.modelmanager.service.base.IBaseService;

import java.io.IOException;

public interface IDataSourceService extends IBaseService<Datasource> {

    void create(Datasource datasource) throws IOException;

    void delete(Datasource datasource) throws IOException;
}
