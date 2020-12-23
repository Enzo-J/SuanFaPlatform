package com.zkwg.modelmanager.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zkwg.modelmanager.entity.Container;
import com.zkwg.modelmanager.service.base.IBaseService;

import java.util.List;

public interface IContainerService extends IBaseService<Container> {

    void updateByNameEN(Container param);

    void updateStatusByNameEN(List<Container> containers);

    IPage<Container> findPage(int pageNum, int pageSize, Wrapper<Container> wrapper);
}
