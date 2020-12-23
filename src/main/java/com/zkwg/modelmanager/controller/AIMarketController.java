package com.zkwg.modelmanager.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zkwg.modelmanager.entity.*;
import com.zkwg.modelmanager.request.ModelRequest;
import com.zkwg.modelmanager.request.PageInfoRequest;
import com.zkwg.modelmanager.request.ServerRequest;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.service.IModelService;
import com.zkwg.modelmanager.service.IServerService;
import com.zkwg.modelmanager.service.ISubscribeService;
import com.zkwg.modelmanager.utils.SecurityUtil;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/market")
public class AIMarketController {

    private static Logger logger = LoggerFactory.getLogger(AIMarketController.class);

    @Autowired
    private IModelService modelService;

    @Autowired
    private IServerService serverService;

    @Autowired
    private ISubscribeService subscribeService;

    /**
     * AI市场模型列表
     */
    @PostMapping("/model")
    public R model(@RequestBody ModelRequest modelRequest) {

//        LambdaQueryWrapper<Model> queryWrapper = new QueryWrapper<Model>()
//                                                .lambda()
//                                                .like(Model::getIsPublish, 1)
//                                                .eq(Model::getType, modelRequest.getType())
//                                                .eq(Model::getIsDelete, 0)
//                                                .orderByAsc(Model::getCreatetime);
//        IPage<Model> page = modelService.findByPage(modelRequest.getPageNum(), modelRequest.getPageSize(),queryWrapper);

        modelRequest.setUserId(SecurityUtil.getUser().getUserId());
        IPage<Model> page = modelService.publicModelList(modelRequest.getPageNum(), modelRequest.getPageSize(), modelRequest);

        return ResultUtil.success(page);
    }

    /**
     * AI市场服务列表
     */
//    @PostMapping("/server")
//    public R server(@RequestBody ServerRequest serverRequest) {
//
//        LambdaQueryWrapper<Server> queryWrapper = new QueryWrapper<Server>()
//                .lambda()
//                .like(Server::getIsPublish, 1)
//                .eq(Server::getType, serverRequest.getType())
//                .orderByAsc(Server::getCreatetime);
//        IPage<Server> page = serverService.findByPage(serverRequest.getPageNum(), serverRequest.getPageSize(),queryWrapper);
//
//        return ResultUtil.success(page);
//    }

    /**
     * 获取模型类型
     */
//    @GetMapping("/model/type/{flag}")
//    public R getModelType(@PathVariable("flag") int flag) {
//        List<Map<Integer,String>> modelTypeList = modelService.getModelType(flag);
//        return ResultUtil.success(modelTypeList);
//    }

    /**
     * AI市场服务列表
     */
    @PostMapping("/server")
    public R<T> server(@RequestBody ServerRequest serverRequest){

//        LambdaQueryWrapper<Server> queryWrapper = new QueryWrapper<Server>()
//                                                .lambda()
//                                                .eq(Server::getIsPublish, 1)
//                                                .eq(Server::getIsDelete, 0)
//                                                .eq(Server::getBusinessType, serverRequest.getType())
//                                                .orderByAsc(Server::getCreatetime);
//        IPage<Server> tenantIPage = serverService.findByPage(serverRequest.getPageNum(), serverRequest.getPageSize(),queryWrapper);

        serverRequest.setUserId(SecurityUtil.getUser().getUserId());
        IPage<Server> serverIPage = serverService.publicServerList(serverRequest.getPageNum(), serverRequest.getPageSize(), serverRequest);
        return ResultUtil.success(serverIPage);
    }


    /**
     * 订阅服务
     */
    @PostMapping("/subscribe/server/{id}")
    public R<T> subscribeServer(@PathVariable("id") Integer id) {
        subscribeService.subscribe(id,3, SecurityUtil.getUser());
        return ResultUtil.success();
    }

    /**
     * 取消订阅服务
     */
    @PostMapping("/unsubscribe/server/{id}")
    public R<T> unsubscribeServer(@PathVariable("id") Integer id) {
        subscribeService.unsubscribe(id,3, SecurityUtil.getUser());
        return ResultUtil.success();
    }

    /**
     * 订阅模型
     */
    @PostMapping("/subscribe/model/{id}")
    public R<T> subscribeModel(@PathVariable("id") Integer id) {
        subscribeService.subscribe(id,2, SecurityUtil.getUser());
        return ResultUtil.success();
    }

    /**
     * 订阅模型
     */
    @PostMapping("/unsubscribe/model/{id}")
    public R<T> unsubscribeModel(@PathVariable("id") Integer id) {
        subscribeService.unsubscribe(id,2, SecurityUtil.getUser());
        return ResultUtil.success();
    }

}
