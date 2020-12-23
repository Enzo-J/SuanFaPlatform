package com.zkwg.modelmanager.service.strategy.impl;

import com.zkwg.modelmanager.service.strategy.AbstractDataScopeHandler;
import com.zkwg.modelmanager.service.IOrganizationService;
import com.zkwg.modelmanager.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 本级以及子级
 *
 */
@Component("THIS_LEVEL_CHILDREN")
public class ThisLevelChildrenDataScope implements AbstractDataScopeHandler {
    @Autowired
    private IUserService userService;
    @Autowired
    private IOrganizationService orgService;

    @Override
    public List<Integer> getOrgIds(List<Integer> orgList, Integer userId) {
//        User user = userService.getById(userId);
//        if (user == null) {
//            return Collections.emptyList();
//        }
//        Long orgId = RemoteData.getKey(user.getOrg());
//        List<Org> children = orgService.findChildren(Arrays.asList(orgId));
//        return children.stream().mapToLong(Org::getId).boxed().collect(Collectors.toList());
        return null;
    }
}
