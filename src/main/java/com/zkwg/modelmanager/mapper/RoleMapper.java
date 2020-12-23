package com.zkwg.modelmanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkwg.modelmanager.entity.Role;
import com.zkwg.modelmanager.request.RoleRequest;
import com.zkwg.modelmanager.security.dataScope.DataScope;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> selectUserRoleListByUserId(int userId);

    IPage<Role> getRolesPage(Page<T> tPage, @Param("param") RoleRequest roleRequest, DataScope dataScope);
}