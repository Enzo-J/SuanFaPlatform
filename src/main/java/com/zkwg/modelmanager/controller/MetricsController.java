package com.zkwg.modelmanager.controller;

import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.utils.K8sClientUtils;
import com.zkwg.modelmanager.utils.K8sProperties;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    private static Logger logger = LoggerFactory.getLogger(MetricsController.class);

    @Autowired
    private K8sProperties k8sProperties;


    /**
     * 获取可用CPU和内存
     */
    @PostMapping("/free")
    public R<T> free(){
        return ResultUtil.success(K8sClientUtils.getInstance(k8sProperties).getFreeCpuAndMemory());
    }



}
