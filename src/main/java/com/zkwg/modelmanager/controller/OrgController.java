package com.zkwg.modelmanager.controller;


import com.zkwg.modelmanager.entity.Organization;
import com.zkwg.modelmanager.entity.Tenant;
import com.zkwg.modelmanager.request.OrgRequest;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.security.AISecurityUser;
import com.zkwg.modelmanager.service.IOrganizationService;
import com.zkwg.modelmanager.utils.SecurityUtil;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/org")
public class OrgController {

    @Autowired
    private IOrganizationService organizationService;


    @RequestMapping("/list")
    public R list() {
        AISecurityUser securityUser = SecurityUtil.getUser();
        return ResultUtil.success(organizationService.selectOrgTree(securityUser.getUserId()));
    }

    @PostMapping("/search")
    public R<T> search(@RequestBody OrgRequest orgRequest){

//        PageInfoRequest pageInfoRequest = new PageInfoRequest();
//        BeanUtils.copyProperties(orgRequest, pageInfoRequest);
//        //
//        Example example = new Example(Role.class);
//        example.createCriteria()
//                .andLike("name","%"+ orgRequest.getName() +"%");
////                .andEqualTo("is_delete",0);
//
//        PageInfo<Role> pageInfo = organizationService.findByPage(pageInfoRequest,example);
        return ResultUtil.success();
    }


//    @PreAuthorize("hasAuthority('sys:menu:add')")
    @PostMapping("/create")
    public R create(@RequestBody OrgRequest orgRequest) {
        Organization organization = deserializeOrg(orgRequest);
        organization.setCreateTime(new Date());
        organization.setCreator(SecurityUtil.getUser().getUserId());
        organization.setStatus((byte) 1);
        organization.setIsDelete((byte) 0);
        organizationService.insertSelective(organization);
        return  ResultUtil.success();
    }

    /**
     * 修改组织机构
     */
//    @PreAuthorize("hasAuthority('sys:menu:update')")
//    @SysOperaLog(descrption = "修改菜单")
    @PostMapping("/update/{id}")
    public R update(@PathVariable Integer id, @RequestBody OrgRequest orgRequest) {
        Organization organization = deserializeOrg(orgRequest);
        organization.setId(id);
        organization.setUpdateTime(new Date());
        organizationService.updateById(organization);
        return  ResultUtil.success();
    }

    /**
     * 根据id删除
     */
//    @PreAuthorize("hasAuthority('sys:menu:delete')")
//    @SysOperaLog(descrption = "删除菜单")
    @RequestMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id) {
        Organization organization = organizationService.selectByPrimaryKey(id);
        Assert.notNull(organization, "无法找到组织机构");
        organization.setIsDelete((byte) 1);
        return ResultUtil.success(organizationService.updateByPrimaryKeySelective(organization));
    }


    private Organization deserializeOrg(OrgRequest orgRequest) {
        Organization organization = new Organization();
        BeanUtils.copyProperties(orgRequest,organization);
        return organization;
    }

}

