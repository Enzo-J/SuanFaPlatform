package com.zkwg.modelmanager.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkwg.modelmanager.entity.Container;
import com.zkwg.modelmanager.security.dataScope.DataScope;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContainerMapper extends BaseMapper<Container> {

    void updateByNameEN(Container param);

    void updateStatusByNameEN(@Param("containers") List<Container> containers);

    IPage<Container> findPage(Page page, @Param(Constants.WRAPPER) Wrapper<Container> wrapper, DataScope dataScope);
}