package com.zkwg.modelmanager.service.impl;

import com.zkwg.modelmanager.entity.Dict;
import com.zkwg.modelmanager.entity.DictItem;
import com.zkwg.modelmanager.mapper.DictItemMapper;
import com.zkwg.modelmanager.mapper.DictMapper;
import com.zkwg.modelmanager.service.IDictItemService;
import com.zkwg.modelmanager.service.IDictService;
import com.zkwg.modelmanager.service.base.BaseService;
import com.zkwg.modelmanager.service.strategy.DataScopeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DictItemServiceImpl extends BaseService<DictItemMapper, DictItem> implements IDictItemService {

    private static Logger logger = LoggerFactory.getLogger(DictItemServiceImpl.class);

    private DictItemMapper dictItemMapper;

    @Autowired
    public void setModelMapper(DictItemMapper dictItemMapper) {
        this.dictItemMapper = dictItemMapper;
        this.baseMapper = dictItemMapper;
    } 

}
