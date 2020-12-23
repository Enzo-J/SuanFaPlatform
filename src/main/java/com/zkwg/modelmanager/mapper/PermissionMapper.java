package com.zkwg.modelmanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zkwg.modelmanager.entity.Permission;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionMapper extends BaseMapper<Permission> {

    List<String> findPermsByUserId(Integer userId);

    List<Permission> selectPermisstionsByUserId(Integer userId);

    @Select("select p.* from role_permission rp join permission p on rp.permission_id = p.id where rp.role_id = #{roleId}")
    List<Permission> selectPermissionsByRoleId(Integer roleId);
}