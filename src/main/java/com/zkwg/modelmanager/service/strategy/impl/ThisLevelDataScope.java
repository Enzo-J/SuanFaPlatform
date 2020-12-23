package com.zkwg.modelmanager.service.strategy.impl;

import com.zkwg.modelmanager.service.strategy.AbstractDataScopeHandler;
import com.zkwg.modelmanager.entity.User;
import com.zkwg.modelmanager.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 本级
 * @author zuihou
 * @version 1.0
 * @date 2019-06-08 15:44
 */
@Component("THIS_LEVEL")
public class ThisLevelDataScope implements AbstractDataScopeHandler {
    @Autowired
    private IUserService userService;

    @Override
    public List<Integer> getOrgIds(List<Integer> orgList, Integer userId) {
        User user = userService.getById(userId);
        if (user == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(user.getId());
    }
}
