package com.zkwg.modelmanager.service;

import com.zkwg.modelmanager.entity.DataSet;
import com.zkwg.modelmanager.request.DatasetRequest;
import com.zkwg.modelmanager.service.base.IBaseService;

import java.io.IOException;

public interface IDataSetService extends IBaseService<DataSet> {

    void create(DataSet dataSet, DatasetRequest datasetRequest) throws IOException;

    void delete(DataSet dataSet) throws IOException;

    String log(DataSet dataSet) throws IOException;

    void sync(DataSet dataSet);
}
