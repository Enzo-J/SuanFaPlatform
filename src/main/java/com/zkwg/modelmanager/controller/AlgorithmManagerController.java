package com.zkwg.modelmanager.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.zkwg.modelmanager.entity.*;
import com.zkwg.modelmanager.request.AlgorithmRepoRequest;
import com.zkwg.modelmanager.request.AlgorithmRequest;
import com.zkwg.modelmanager.request.ContainerRequest;
import com.zkwg.modelmanager.request.PageInfoRequest;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.security.dataScope.DataScope;
import com.zkwg.modelmanager.service.IAlgorithmRepoService;
import com.zkwg.modelmanager.service.IAlgorithmService;
import com.zkwg.modelmanager.service.IAlgorithmTypeService;
import com.zkwg.modelmanager.service.IContainerService;
import com.zkwg.modelmanager.utils.*;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/algorithm")
public class AlgorithmManagerController {

    private static Logger logger = LoggerFactory.getLogger(AlgorithmManagerController.class);


    @Autowired
    private IAlgorithmService algorithmService;

    @Autowired
    private IAlgorithmTypeService algorithmTypeService;

    @Autowired
    private IAlgorithmRepoService algorithmRepoService;

    /**
     * 创建算法实例
     */
    @PostMapping("/create")
    public R<T> create(@RequestBody AlgorithmRequest algorithmRequest){
        Algorithm algorithm = deserializeAlgorithmRequest(algorithmRequest);
        algorithmService.insertSelective(algorithm);
        return ResultUtil.success(algorithm);
    }

    @PostMapping("/type")
    public R<T> types(){
        return ResultUtil.success(algorithmTypeService.selectAll());
    }

    @PostMapping("/type/{id:[0-9]*}")
    public R<T> type(@PathVariable("id") Integer id){
        AlgorithmType algorithmType = algorithmTypeService.selectByPrimaryKey(id);
        return ResultUtil.success(algorithmType);
    }


    /**
     * 查找容器
     */
    @PostMapping("/search")
    public R<T> search(@RequestBody AlgorithmRequest algorithmRequest){

        LambdaQueryWrapper<Algorithm> queryWrapper = new QueryWrapper<Algorithm>()
                .lambda()
                .like(Algorithm::getName, algorithmRequest.getName())
                .eq(Algorithm::getIsDelete, 0)
                .orderByAsc(Algorithm::getCreateTime);
        IPage<Algorithm> algorithmIPage = algorithmService.findByPage(algorithmRequest.getPageNum(), algorithmRequest.getPageSize(),queryWrapper);

        return ResultUtil.success(algorithmIPage);
    }

    @PostMapping("/search/{id:[0-9]*}")
    public R<T> search(@PathVariable int id){
        return ResultUtil.success(algorithmService.selectByPrimaryKey(id));
    }

    /**
     * 删除算法
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete/{id:[0-9]*}")
    public R delete(@PathVariable int id){
        Algorithm algorithm = algorithmService.selectByPrimaryKey(id);
        algorithm.setIsDelete(1);
        algorithmService.updateByPrimaryKeySelective(algorithm);
        return ResultUtil.success();
    }

    /**
     * 更新算法
     *
     * @param algorithmRequest
     * @return
     */
    @PostMapping("/update")
    public R<T> update(@RequestBody AlgorithmRequest algorithmRequest){
        Algorithm algorithm = deserializeAlgorithmRequest(algorithmRequest);
        algorithmService.updateByPrimaryKeySelective(algorithm);
        return ResultUtil.success();
    }

    @PostMapping("/repo")
    public R<T> Repository(@RequestBody AlgorithmRepoRequest algorithmRepoRequest){
        //algorithmRepoService.selectAlgorithmRepository()
        IPage<AlgorithmRepo> algorithmRepoIPage = algorithmRepoService.getAlgorithmRepoVosPage(algorithmRepoRequest.getPageNum(), algorithmRepoRequest.getPageSize(),algorithmRepoRequest);

        return ResultUtil.success(algorithmRepoIPage);
    }


//
//    private void setContainerMetrics(List<Container> list) {
//        for (Container container : list) {
//            if(StringUtils.isBlank(container.getPodName()) || StringUtils.isBlank(container.getNamespace())) continue;
//            try {
//                K8sClientUtils.getInstance(k8sProperties).setContainerMemory(container.getNamespace(),container.getPodName(),container);
//            } catch (Exception e) {
//                logger.error("设置CPU、内存失败",e);
//            }
//        }
//    }
//
//
//    /**
//     * 删除容器
//     */
//    @RequestMapping("/delete/{id}")
//    public R delete(@PathVariable int id){
//        Container container = containerService.selectByPrimaryKey(id);
////        Container container = checkContainerStatus(id,Operator.DELETE);
//        container.setIsDelete((byte)1);
//        containerService.updateByPrimaryKeySelective(container);
//        return ResultUtil.success();
//    }
//
//    @PostMapping("/update/{id:[0-9]*}")
//    public R<T> update(@PathVariable("id") int id){
//        Container container = checkContainerStatus(id, Operator.UPDATE);
//        return ResultUtil.success();
//    }
//
//    /**
//     * 分配容器
//     */
//    @GetMapping("/allocation/{id:[0-9]*}")
//    public R<T> allocation(@PathVariable("id") int id){
////        checkServerStatus(id,0);
//        return ResultUtil.success();
//    }
//
//    /**
//     * 性能检测
//     */
//    @GetMapping("/monitor/{id:[0-9]*}")
//    public R<T> monitor(@PathVariable("id") int id){
//
//        Container container = checkContainerStatus(id, Operator.MONITOR);
//
//        Map<String,Object> params = Maps.newHashMap();// ImmutableMap.of("","1","","");
//        params.put("orgId","1");
//        params.put("refresh","10s");
//        params.put("var-datasource","default");
//        params.put("var-cluster","");
//        params.put("var-namespace",container.getNamespace());
//        params.put("var-pod",container.getPodName());
////        params.put("kiosk","tv");
//
//        String pod = UrlUtils.appendUrl(podUrl,params);
//
//        return ResultUtil.success(ImmutableMap.of("url",pod+"&kiosk"));
//    }
//
//
//    /**
//     * 根据状态判定操作是否可以执行
//     * @param id 资源池ID
//     * @param operator 检测操作：1.删除
//     */
//    private Container checkContainerStatus(int id,Operator operator) {
//
//        Container container = containerService.selectByPrimaryKey(id);
//        Assert.notNull(container,"容器不存在！");
//
//        byte status = container.getStatus();
//        // 1.未分配 2.已分配 3.运行中 4.超时 5.失败 6.停止
//        switch (operator) {
//            case CREATE:
//                break;
//            case DELETE:
//                if(status != 1){
//                    throw new RuntimeException("容器已分配不能删除！");
//                }
//                break;
//            case UPDATE:
//                if(status != 1){
//                    throw new RuntimeException("容器已分配不能修改！");
//                }
//                break;
//            case MONITOR:
//                if(status != 3){
//                    throw new RuntimeException("容器未运行，不能监控！");
//                }
//                break;
//        }
//
//        return container;
//    }
//
//
    /**
     * 解析AlgorithmRequest参数
     * @param algorithmRequest
     * @return
     */
    private Algorithm deserializeAlgorithmRequest(AlgorithmRequest algorithmRequest){
        Algorithm algorithm = new Algorithm();
        BeanUtils.copyProperties(algorithmRequest,algorithm);

        algorithm.setCreateTime(LocalDateTime.now());
        algorithm.setIsDelete(0);
        algorithm.setIsPublish(0);
        algorithm.setStatus(1);
        algorithm.setCreator(SecurityUtil.getUser().getUserId());

        return algorithm;
    }
//
//    private void checkContainerJson(String containerJson) {
//        try {
//            io.fabric8.kubernetes.api.model.Container container =  Serialization.unmarshal(containerJson, io.fabric8.kubernetes.api.model.Container.class);
//            //TODO 检查分配资源是否合理
//            logger.info(container.toString());
//        } catch (Exception e) {
//            throw new RuntimeException("containerJson 格式不正确！");
//        }
//    }

    private enum Operator {
        CREATE,
        DELETE,
        UPDATE,
        MONITOR
    }

}
