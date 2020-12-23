package com.zkwg.modelmanager.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zkwg.modelmanager.entity.Datasource;
import com.zkwg.modelmanager.entity.Tenant;
import com.zkwg.modelmanager.request.DataSourceRequest;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.service.IDataSourceService;
import com.zkwg.modelmanager.utils.DolphinSchedulerClientUtils;
import com.zkwg.modelmanager.utils.SecurityUtil;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/datasource")
public class DataSourceController {

    @Autowired
    private IDataSourceService dataSourceService;

    /**
     * 查看资源池列表
     */
    @PostMapping("/search")
    public R<T> search(@RequestBody DataSourceRequest dataSourceRequest){

//        IPage<Model> modelPage = new Page<>(1, 2);//参数一是当前页，参数二是每页个数
        LambdaQueryWrapper<Datasource> queryWrapper = new QueryWrapper<Datasource>()
                                                .lambda()
//                                                .like(DataSourceRequest::getName, dataSourceRequest.getName())
//                                                .eq( , 0)
                                                .orderByAsc(Datasource::getCreateTime);
        IPage<Datasource> datasourceIPage = dataSourceService.findByPage(dataSourceRequest.getPageNum(), dataSourceRequest.getPageSize(),queryWrapper);
        return ResultUtil.success(datasourceIPage);
    }

    /**
     * 新增租户
     *
     */
//    @SysOperaLog(descrption = "新增租户")
    @PostMapping("/create")
//    @PreAuthorize("hasAuthority('sys:tenant:add')")
    public R create(@RequestBody DataSourceRequest dataSourceRequest) throws Exception {
        Datasource datasource = deserializeDatasource(dataSourceRequest);
        datasource.setCreateTime(LocalDateTime.now());
        datasource.setUuid(UUID.randomUUID().toString().replace("-",""));
        datasource.setCreator(SecurityUtil.getUser().getUserId());
        dataSourceService.create(datasource);
        return ResultUtil.success();
    }

    @RequestMapping("/type/{typeId}")
    public R queryDataTypeList(@PathVariable Integer typeId) throws Exception {
        LambdaQueryWrapper<Datasource> queryWrapper = new QueryWrapper<Datasource>()
                                                      .lambda().eq(Datasource::getType, typeId).gt(Datasource::getId, -1);
        return ResultUtil.success(dataSourceService.select(queryWrapper));
//        return ResultUtil.success(DolphinSchedulerClientUtils.queryDataSourceList(datasourceType));
    }

    private Datasource deserializeDatasource(DataSourceRequest dataSourceRequest) {
        Datasource datasource = new Datasource();
        BeanUtils.copyProperties(dataSourceRequest,datasource);
        return datasource;
    }

    /**
     * 修改租户
     */
////    @SysOperaLog(descrption = "修改租户")
//    @PostMapping("/update/{id}")
////    @PreAuthorize("hasAuthority('sys:tenant:update')")
//    public R update(@PathVariable Integer id, @RequestBody TenantRequest tenantRequest) {
//        Tenant tenant = tenantService.selectByPrimaryKey(id);
//        Assert.notNull(tenant, "无法找到租户");
//        BeanUtils.copyProperties(tenantRequest,tenant);
//        tenantService.updateById(tenant);
//        return ResultUtil.success();
//    }


    /**
     * 通过id删除租户
     */
//    @SysOperaLog(descrption = "删除租户")
    @RequestMapping("/delete/{id}")
//    @PreAuthorize("hasAuthority('sys:tenant:del')")
    public R delete(@PathVariable Integer id) throws IOException {
        Datasource datasource = dataSourceService.selectByPrimaryKey(id);
        Assert.notNull(datasource, "无法找到数据源");
        dataSourceService.delete(datasource);
        return ResultUtil.success();
    }

}

