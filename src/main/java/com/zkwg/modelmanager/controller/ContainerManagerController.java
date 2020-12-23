package com.zkwg.modelmanager.controller;

//import com.github.pagehelper.PageInfo;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.zkwg.modelmanager.entity.Algorithm;
import com.zkwg.modelmanager.entity.Container;
import com.zkwg.modelmanager.request.ContainerRequest;
import com.zkwg.modelmanager.request.PageInfoRequest;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
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
//import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/container")
public class ContainerManagerController {

    private static Logger logger = LoggerFactory.getLogger(ContainerManagerController.class);

    @Value("${grafana.pod_url}")
    private String podUrl;

    @Autowired
    private K8sProperties k8sProperties;

    @Autowired
    private IContainerService containerService;

    /**
     * 创建容器
     */
    @PostMapping("/create")
    public R<T> create(@RequestBody ContainerRequest containerRequest){
        Container container = deserializeContainerRequest(containerRequest);
        container.setCreator(SecurityUtil.getUser().getUserId());
        containerService.insertSelective(container);
        return ResultUtil.success();
    }

    /**
     * 查找容器
     */
    @PostMapping("/search")
    public R<T> search(@RequestBody ContainerRequest containerRequest){

//        pageInfo.getList().stream().map(m -> {
//            if(m.getStatus() == 3) {
//                K8sClientUtils.getInstance(k8sProperties).setContainerMemory(m.getNamespace(),m.getPodName(),m);
//            }
//            return m;
//        }).collect(Collectors.toList());

//        return ResultUtil.success(pageInfo);
        LambdaQueryWrapper<Container> queryWrapper = new QueryWrapper<Container>()
                .lambda()
                .like(StrUtil.isNotBlank(containerRequest.getName()), Container::getName, containerRequest.getName())
                .eq(Container::getIsDelete, 0)
                .orderByDesc(Container::getCreateTime);
        IPage<Container> page = containerService.findPage(containerRequest.getPageNum(), containerRequest.getPageSize(),queryWrapper);


        setContainerMetrics(page.getRecords());


        return ResultUtil.success(page);
    }

    @PostMapping("/search/valid")
    public R<T> searchValid(@RequestBody ContainerRequest containerRequest){

//        pageInfo.getList().stream().map(m -> {
//            if(m.getStatus() == 3) {
//                K8sClientUtils.getInstance(k8sProperties).setContainerMemory(m.getNamespace(),m.getPodName(),m);
//            }
//            return m;
//        }).collect(Collectors.toList());

//        return ResultUtil.success(pageInfo);

        // 1、查询训练作业选择的容器
        // 2、查询所有未分配的可用容器
        // e.g.
        //    SELECT * FROM container WHERE (id = ? OR (name LIKE ? AND is_delete = ? AND status = ? )) AND tenant_id = ?;
        LambdaQueryWrapper<Container> queryWrapper = new QueryWrapper<Container>()
                .lambda()
                .eq(Container::getId, containerRequest.getId())
                .or(wrapper -> wrapper
                        .like(Container::getName, containerRequest.getName())
                        .eq(Container::getIsDelete, 0)
                        .eq(Container::getStatus, 1)
                        )
                .orderByAsc(Container::getCreateTime);
        IPage<Container> page = containerService.findByPage(containerRequest.getPageNum(), containerRequest.getPageSize(),queryWrapper);

        setContainerMetrics(page.getRecords());

        return ResultUtil.success(page);
    }

    private void setContainerMetrics(List<Container> list) {
        for (Container container : list) {
            if(container.getStatus() != 3) continue;
            try {
                K8sClientUtils.getInstance(k8sProperties).setContainerMemory(container.getNamespace(),container.getPodName(),container);
            } catch (Exception e) {
                logger.info("" ,e);
            }
        }
    }


    /**
     * 删除容器
     */
    @RequestMapping("/delete/{id}")
    public R delete(@PathVariable int id){
        Container container = containerService.selectByPrimaryKey(id);
//        Container container = checkContainerStatus(id,Operator.DELETE);
        container.setIsDelete((byte)1);
        containerService.updateByPrimaryKeySelective(container);
        return ResultUtil.success();
    }

    @PostMapping("/update/{id:[0-9]*}")
    public R<T> update(@PathVariable("id") int id){
        Container container = checkContainerStatus(id,Operator.UPDATE);
        return ResultUtil.success();
    }

    /**
     * 分配容器
     */
    @GetMapping("/allocation/{id:[0-9]*}")
    public R<T> allocation(@PathVariable("id") int id){
//        checkServerStatus(id,0);
        return ResultUtil.success();
    }

    /**
     * 性能检测
     */
    @GetMapping("/monitor/{id:[0-9]*}")
    public R<T> monitor(@PathVariable("id") int id){

        Container container = checkContainerStatus(id,Operator.MONITOR);

        Map<String,Object> params = Maps.newHashMap();// ImmutableMap.of("","1","","");
        params.put("orgId","1");
        params.put("refresh","10s");
        params.put("var-datasource","default");
        params.put("var-cluster","");
        params.put("var-namespace",container.getNamespace());
        params.put("var-pod",container.getPodName());
//        params.put("kiosk","tv");

        String pod = UrlUtils.appendUrl(podUrl,params);

        return ResultUtil.success(ImmutableMap.of("url",pod+"&kiosk"));
    }


    /**
     * 根据状态判定操作是否可以执行
     * @param id 资源池ID
     * @param operator 检测操作：1.删除
     */
    private Container checkContainerStatus(int id,Operator operator) {

        Container container = containerService.selectByPrimaryKey(id);
        Assert.notNull(container,"容器不存在！");

        byte status = container.getStatus();
        // 1.未分配 2.已分配 3.运行中 4.超时 5.失败 6.停止
        switch (operator) {
            case CREATE:
                break;
            case DELETE:
                if(status != 1){
                    throw new RuntimeException("容器已分配不能删除！");
                }
                break;
            case UPDATE:
                if(status != 1){
                    throw new RuntimeException("容器已分配不能修改！");
                }
                break;
            case MONITOR:
                if(status != 3){
                    throw new RuntimeException("容器未运行，不能监控！");
                }
                break;
        }

        return container;
    }


    /**
     * 解析ContainerRequest参数
     * @param containerRequest
     * @return
     */
    private Container deserializeContainerRequest(ContainerRequest containerRequest){
        Container container = new Container();
        BeanUtils.copyProperties(containerRequest,container);
//        checkContainerJson(container.getContainerJson());
        io.fabric8.kubernetes.api.model.Container k8sContainer =  Serialization.unmarshal(container.getContainerJson(), io.fabric8.kubernetes.api.model.Container.class);
        k8sContainer.setName(PingYinTools.getPinYinHeadChar(containerRequest.getName()));
        //
        container.setContainerJson(Serialization.asJson(k8sContainer));
        container.setCreateTime(new Date());
        container.setNameEn(PingYinTools.getPinYinHeadChar(containerRequest.getName()));
        container.setStatus((byte) 1); // 1.未分配 2.已分配 3.运行中
        container.setIsDelete((byte) 0);
        return container;
    }

    private void checkContainerJson(String containerJson) {
        try {
            io.fabric8.kubernetes.api.model.Container container =  Serialization.unmarshal(containerJson, io.fabric8.kubernetes.api.model.Container.class);
            //TODO 检查分配资源是否合理
            logger.info(container.toString());
        } catch (Exception e) {
            throw new RuntimeException("containerJson 格式不正确！");
        }
    }

    private enum Operator {
        CREATE,
        DELETE,
        UPDATE,
        MONITOR
    }

}
