package com.zkwg.modelmanager.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkwg.modelmanager.entity.Server;
import com.zkwg.modelmanager.request.ServerRequest;
import com.zkwg.modelmanager.security.dataScope.DataScope;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerMapper extends BaseMapper<Server> {

    IPage<Server> subscribeList(Page<T> tPage, @Param("param") ServerRequest serverRequest, DataScope dataScope);

    IPage<Server> publicServerList(Page<T> tPage, @Param("param") ServerRequest serverRequest);

    IPage<Server> findPage(Page page, @Param(Constants.WRAPPER) Wrapper<Server> wrapper, DataScope dataScope);
}