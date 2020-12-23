package com.zkwg.modelmanager.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkwg.modelmanager.entity.*;
import com.zkwg.modelmanager.mapper.TestCaseMapper;
import com.zkwg.modelmanager.request.AlgorithmRequest;
import com.zkwg.modelmanager.request.DataSourceRequest;
import com.zkwg.modelmanager.request.DatasetRequest;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.service.IAlgorithmService;
import com.zkwg.modelmanager.service.IDataSetService;
import com.zkwg.modelmanager.utils.SecurityUtil;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/dataset")
public class DatasetManagerController {
    private static Logger logger = LoggerFactory.getLogger(DatasetManagerController.class);

    @Autowired
    private IDataSetService dataSetService;

    // 为了演示，临时增加
    @Autowired
    private TestCaseMapper testCaseMapper;

    /**
     * 查找容器
     */
    @PostMapping("/search")
    public R<T> search(@RequestBody DatasetRequest datasetRequest){

        LambdaQueryWrapper<DataSet> queryWrapper = new QueryWrapper<DataSet>()
                .lambda()
                .eq(datasetRequest.getType() != null, DataSet::getType, datasetRequest.getType())
                .eq(DataSet::getIsDelete, 0)
                .like(StrUtil.isNotBlank(datasetRequest.getName()), DataSet::getName, datasetRequest.getName())
                .orderByAsc(DataSet::getCreateTime);
        IPage<DataSet> dataSetIPage = dataSetService.findByPage(datasetRequest.getPageNum(), datasetRequest.getPageSize(),queryWrapper);

        return ResultUtil.success(dataSetIPage);
    }

    @PostMapping("/search/{id:[0-9]*}")
    public R<T> search(@PathVariable int id){
        return ResultUtil.success(dataSetService.selectByPrimaryKey(id));
    }

    @PostMapping("/create")
    public R<T> create(@RequestBody DatasetRequest datasetRequest) throws IOException {
        DataSet dataSet = deserializeDataSet(datasetRequest);
        dataSet.setCreator(SecurityUtil.getUser().getUserId());
        dataSet.setCreateTime(LocalDateTime.now());
        dataSet.setIsDelete(0);
        dataSetService.create(dataSet, datasetRequest);
        return ResultUtil.success();
    }

    @RequestMapping("/delete/{id}")
    public R<T> delete(@PathVariable Integer id) throws IOException {
        DataSet dataSet = dataSetService.selectByPrimaryKey(id);
        Assert.notNull(dataSet, "无法找到数据集");
        dataSetService.delete(dataSet);
        return ResultUtil.success();
    }

    @RequestMapping("/log/{id}")
    public R<T> log(@PathVariable Integer id) throws IOException {
        DataSet dataSet = dataSetService.selectByPrimaryKey(id);
        Assert.notNull(dataSet, "无法找到数据集");
        return ResultUtil.success(dataSetService.log(dataSet));
    }

    @RequestMapping("/sync/{id}")
    public R<T> sync(@PathVariable Integer id) throws IOException {
        DataSet dataSet = dataSetService.selectByPrimaryKey(id);
        Assert.notNull(dataSet, "无法找到数据集");
        dataSetService.sync(dataSet);
        return ResultUtil.success();
    }

    @RequestMapping("/detail/{id}")
    public R<T> detail(@PathVariable Integer id) throws IOException {
        DataSet dataSet = dataSetService.selectByPrimaryKey(id);
        Assert.notNull(dataSet, "无法找到数据集");
        return ResultUtil.success(dataSet);
    }

    /**
     * 为了演示，临时增加
     * @return
     * @throws IOException
     */
    @RequestMapping("/looking")
    public R<T> looking(@RequestBody DatasetRequest datasetRequest) {
        IPage<TestCase> page = testCaseMapper.selectPage(new Page<TestCase>(datasetRequest.getPageNum(), datasetRequest.getPageSize()),null);
        return ResultUtil.success(page);
    }

    private DataSet deserializeDataSet(DatasetRequest datasetRequest) {
        DataSet dataSet = new DataSet();
        BeanUtils.copyProperties(datasetRequest,dataSet);
        return dataSet;
    }

}
