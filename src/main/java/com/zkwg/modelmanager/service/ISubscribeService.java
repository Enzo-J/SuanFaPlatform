package com.zkwg.modelmanager.service;


import com.zkwg.modelmanager.entity.Subscribe;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.security.AISecurityUser;
import com.zkwg.modelmanager.service.base.IBaseService;
import org.apache.poi.ss.formula.functions.T;

public interface ISubscribeService extends IBaseService<Subscribe> {

    void subscribe(Integer id, int type, AISecurityUser user);

    void unsubscribe(Integer targetId, int type, AISecurityUser user);
}
