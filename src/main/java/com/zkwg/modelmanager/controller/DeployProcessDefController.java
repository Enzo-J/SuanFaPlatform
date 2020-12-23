package com.zkwg.modelmanager.controller;

import com.zkwg.modelmanager.service.IDeployProcessDefService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deployProcessDef")
public class DeployProcessDefController {

    private static Logger logger = LoggerFactory.getLogger(DeployProcessDefController.class);

    @Autowired
    private IDeployProcessDefService deployProcessDefService;

//    /**
//     * 运行
//     */
//    @GetMapping("/run/{id}")
//    public R run(@PathVariable("id") int id) throws Exception {
//        return deployProcessDefService.run(id);
//    }



}
