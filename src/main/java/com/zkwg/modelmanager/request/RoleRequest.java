package com.zkwg.modelmanager.request;


import com.zkwg.modelmanager.entity.Permission;
import com.zkwg.modelmanager.security.dataScope.DataScopeType;

import java.util.List;

public class RoleRequest extends PageInfoRequest {

    private Integer id;

    private String name;

    private Integer type;

    private DataScopeType dsType;

    private List<Integer> orgList;

    private List<Integer> permissionIds;

    public RoleRequest() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public DataScopeType getDsType() {
        return dsType;
    }

    public void setDsType(DataScopeType dsType) {
        this.dsType = dsType;
    }

    public List<Integer> getOrgList() {
        return orgList;
    }

    public void setOrgList(List<Integer> orgList) {
        this.orgList = orgList;
    }

    public List<Integer> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Integer> permissionIds) {
        this.permissionIds = permissionIds;
    }
}
