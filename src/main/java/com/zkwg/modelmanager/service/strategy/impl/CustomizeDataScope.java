package com.zkwg.modelmanager.service.strategy.impl;

import com.zkwg.modelmanager.service.IOrganizationService;
import com.zkwg.modelmanager.service.strategy.AbstractDataScopeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义模式
 *
 */
@Component("CUSTOMIZE")
public class CustomizeDataScope implements AbstractDataScopeHandler {

    @Autowired
    private IOrganizationService orgService;

    @Override
    public List<Integer> getOrgIds(List<Integer> orgList, Integer userId) {
//        if (orgList == null || orgList.isEmpty()) {
//            throw new BizException(ExceptionCode.BASE_VALID_PARAM.getCode(), "自定义数据权限类型时，组织不能为空");
//        }
//        List<Org> children = orgService.findChildren(orgList);
//        return children.stream().mapToLong(Org::getId).boxed().collect(Collectors.toList());
        return null;
    }
}
