package com.zkwg.modelmanager.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkwg.modelmanager.entity.Model;
import com.zkwg.modelmanager.entity.Tenant;
import com.zkwg.modelmanager.request.PageInfoRequest;
import com.zkwg.modelmanager.request.ResourcePoolRequest;
import com.zkwg.modelmanager.request.TenantRequest;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.service.ITenantService;
import com.zkwg.modelmanager.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 租户表 前端控制器
 */
@RestController
@RequestMapping("/tenant")
public class TenantController {

    @Autowired
    private ITenantService tenantService;

    /**
     * 查看资源池列表
     */
    @PostMapping("/search")
    public R<T> search(@RequestBody TenantRequest tenantRequest){

//        IPage<Model> modelPage = new Page<>(1, 2);//参数一是当前页，参数二是每页个数
        LambdaQueryWrapper<Tenant> queryWrapper = new QueryWrapper<Tenant>()
                                                .lambda()
                                                .like(StrUtil.isNotBlank(tenantRequest.getTenantName()), Tenant::getTenantName, tenantRequest.getTenantName())
                                                .eq(Tenant::getIsDelete, 0)
                                                .orderByAsc(Tenant::getCreateTime);
        IPage<Tenant> tenantIPage = tenantService.findByPage(tenantRequest.getPageNum(), tenantRequest.getPageSize(),queryWrapper);
        return ResultUtil.success(tenantIPage);
    }

    /**
     * 新增租户
     *
     */
//    @SysOperaLog(descrption = "新增租户")
    @PostMapping("/create")
//    @PreAuthorize("hasAuthority('sys:tenant:add')")
    public R create(@RequestBody TenantRequest tenantRequest) {
        Tenant tenant = deserializeTenant(tenantRequest);
        tenant.setIsDelete((byte) 0);
        tenant.setCreateTime(new Date());
        tenant.setStatus((byte) 1);
        tenantService.insertSelective(tenant);
        return ResultUtil.success();
    }

    @RequestMapping("/init/{id}")
//    @PreAuthorize("hasAuthority('sys:tenant:del')")
    public R init(@PathVariable Integer id) {
        Tenant tenant = tenantService.selectByPrimaryKey(id);
        Assert.notNull(tenant, "无法找到租户");
        tenantService.init(tenant);
        return ResultUtil.success();
    }

    private Tenant deserializeTenant(TenantRequest tenantRequest) {
        Tenant tenant = new Tenant();
        BeanUtils.copyProperties(tenantRequest,tenant);
        return tenant;
    }

    /**
     * 修改租户
     */
//    @SysOperaLog(descrption = "修改租户")
    @PostMapping("/update/{id}")
//    @PreAuthorize("hasAuthority('sys:tenant:update')")
    public R update(@PathVariable Integer id, @RequestBody TenantRequest tenantRequest) {
        Tenant tenant = tenantService.selectByPrimaryKey(id);
        Assert.notNull(tenant, "无法找到租户");
        BeanUtils.copyProperties(tenantRequest,tenant);
        tenantService.updateById(tenant);
        return ResultUtil.success();
    }


    /**
     * 通过id删除租户
     */
//    @SysOperaLog(descrption = "删除租户")
    @RequestMapping("/delete/{id}")
//    @PreAuthorize("hasAuthority('sys:tenant:del')")
    public R delete(@PathVariable Integer id) {
        Tenant tenant = tenantService.selectByPrimaryKey(id);
        Assert.notNull(tenant, "无法找到租户");
        tenant.setIsDelete((byte) 1);
        tenant.setStatus((byte) 4);
        return ResultUtil.success(tenantService.updateByPrimaryKeySelective(tenant));
    }
//
//    /**
//     * 设置租户id -- 主要是第三方登录使用 目的需要发送请求 看看后面有没有办法解决
//     *
//     * @param tenantId
//     */
//    @PostMapping("/setting/{tenantId}")
//    public R setting(@PathVariable Integer tenantId) {
//        return R.ok();
//    }


}

