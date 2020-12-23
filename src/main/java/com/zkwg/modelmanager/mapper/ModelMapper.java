package com.zkwg.modelmanager.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkwg.modelmanager.entity.Model;
import com.zkwg.modelmanager.request.ModelRequest;
import com.zkwg.modelmanager.security.dataScope.DataScope;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ModelMapper extends BaseMapper<Model> {

    void updateStatusByUrls(List<Model> list);

    void updateStatusByUrl(Model model);

    void updateStatusByImage(List<Model> models);

    IPage<Model> subscribeList(Page page, @Param("param") ModelRequest modelRequest, DataScope dataScope);

    IPage<Model> publicModelList(Page<T> tPage, @Param("param") ModelRequest modelRequest);

    IPage<Model> findPage(Page page, @Param(Constants.WRAPPER) Wrapper<Model> wrapper, DataScope dataScope);
}