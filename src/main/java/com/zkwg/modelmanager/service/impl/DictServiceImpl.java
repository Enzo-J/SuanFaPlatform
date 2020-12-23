package com.zkwg.modelmanager.service.impl;

import com.zkwg.modelmanager.entity.Dict;
import com.zkwg.modelmanager.entity.DictItem;
import com.zkwg.modelmanager.mapper.DictMapper;
import com.zkwg.modelmanager.service.IDictService;
import com.zkwg.modelmanager.service.base.BaseService;
import com.zkwg.modelmanager.service.strategy.DataScopeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceImpl extends BaseService<DictMapper, Dict> implements IDictService {

    private static Logger logger = LoggerFactory.getLogger(DictServiceImpl.class);

    private DictMapper dictMapper;

    @Autowired
    public void setModelMapper(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
        this.baseMapper = dictMapper;
    }

    @Override
    public List<DictItem> queryDictItemByDictName(String dictName) {
        return dictMapper.queryDictItemByDictName(dictName);
    }
}
