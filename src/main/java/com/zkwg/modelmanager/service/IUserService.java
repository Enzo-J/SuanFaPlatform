package com.zkwg.modelmanager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zkwg.modelmanager.entity.User;
import com.zkwg.modelmanager.request.UserRequest;
import com.zkwg.modelmanager.security.dataScope.DataScope;
import com.zkwg.modelmanager.service.base.IBaseService;

import java.util.Map;
import java.util.Set;

public interface IUserService extends IBaseService<User> {

    Set<String> findPermsByUserId(Integer userId);

    Set<String> findRoleIdByUserId(int userId);

    User findSecurityUserByUser(User sysUser);

    String login(String username, String password);

    void createUser(UserRequest userRequest);

    Map<String, Object> getDataScopeById(Integer userId);

    void updateUser(UserRequest userRequest);

    IPage<User> getUserVosPage(int pageNum, int pageSize, UserRequest userRequest, DataScope dataScope);

    void updateRunner(UserRequest userRequest);
}
