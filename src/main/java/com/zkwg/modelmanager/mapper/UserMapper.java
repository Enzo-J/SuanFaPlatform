package com.zkwg.modelmanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkwg.modelmanager.entity.User;
import com.zkwg.modelmanager.request.UserRequest;
import com.zkwg.modelmanager.security.dataScope.DataScope;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

@Repository
public interface UserMapper extends BaseMapper<User> {


    /**
     * 分页查询用户信息（含角色）
     */
    IPage<User> getUserVosPage(Page page, @Param("query") UserRequest userRequest, DataScope dataScope);

}