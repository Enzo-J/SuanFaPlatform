package com.zkwg.modelmanager.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zkwg.modelmanager.entity.Model;
import com.zkwg.modelmanager.request.ModelRequest;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.service.base.IBaseService;

import java.util.List;
import java.util.Map;

public interface IModelService extends IBaseService<Model> {


    R deploy(Model model) throws Exception;

    List<Map<Integer, String>> getModelType(int flag);

    void updateStatusByUrl(List<Model> models);

    void updateStatusByUrl(Model model);

    void updateStatusByImage(List<Model> models);

    IPage<Model> subscribeList(Integer pageNum, Integer pageSize, ModelRequest modelRequest);

    IPage<Model> publicModelList(Integer pageNum, Integer pageSize, ModelRequest modelRequest);

    IPage<Model> findPage(int pageNum, int pageSize, Wrapper<Model> wrapper);

    void importModel(Integer trainingId, Model model) throws Exception;
}
