package com.zkwg.modelmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkwg.modelmanager.entity.Container;
import com.zkwg.modelmanager.mapper.ContainerMapper;
import com.zkwg.modelmanager.security.dataScope.DataScope;
import com.zkwg.modelmanager.service.IContainerService;
import com.zkwg.modelmanager.service.base.BaseService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContainerServiceImpl extends BaseService<ContainerMapper, Container> implements IContainerService {

    private static Logger logger = LoggerFactory.getLogger(ContainerServiceImpl.class);


    private ContainerMapper containerMapper;

    @Autowired
    public void setModelMapper(ContainerMapper containerMapper) {
        this.containerMapper = containerMapper;
        this.baseMapper = containerMapper;
    }

    @Override
    public void updateByNameEN(Container param) {
        containerMapper.updateByNameEN(param);
    }

    @Override
    public void updateStatusByNameEN(List<Container> containers) {
        containerMapper.updateStatusByNameEN(containers);
    }

    @Override
    public IPage<Container> findPage(int pageNum, int pageSize, Wrapper<Container> wrapper) {
        return containerMapper.findPage(new Page(pageNum, pageSize), wrapper, new DataScope());
    }

//    IPage<> findPage(IPage<> page, @Param(Constants.WRAPPER) Wrapper<> wrapper, DataScope dataScope);

}
