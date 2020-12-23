package com.zkwg.modelmanager.service;

import com.zkwg.modelmanager.entity.Dict;
import com.zkwg.modelmanager.entity.DictItem;
import com.zkwg.modelmanager.service.base.IBaseService;

import java.util.List;


public interface IDictService extends IBaseService<Dict> {

    List<DictItem> queryDictItemByDictName(String dictName);
}
