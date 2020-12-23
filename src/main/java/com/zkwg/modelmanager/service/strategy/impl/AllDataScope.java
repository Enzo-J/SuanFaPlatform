package com.zkwg.modelmanager.service.strategy.impl;

import com.zkwg.modelmanager.entity.Organization;
import com.zkwg.modelmanager.service.strategy.AbstractDataScopeHandler;
import com.zkwg.modelmanager.service.IOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 所有
 *
 * @author zuihou
 * @version 1.0
 * @date 2019-06-08 16:27
 */
@Component("ALL")
public class AllDataScope implements AbstractDataScopeHandler {

    @Autowired
    private IOrganizationService orgService;

    @Override
    public List<Integer> getOrgIds(List<Integer> orgList, Integer userId) {
        List<Organization> list = orgService.lambdaQuery().select(Organization::getId).list();
        return list.stream().map(Organization::getId).collect(Collectors.toList());
    }


}
