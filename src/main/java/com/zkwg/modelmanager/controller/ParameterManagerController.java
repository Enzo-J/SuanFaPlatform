package com.zkwg.modelmanager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zkwg.modelmanager.entity.Algorithm;
import com.zkwg.modelmanager.entity.AlgorithmParameter;
import com.zkwg.modelmanager.request.AlgorithmRequest;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.service.IAlgorithmParameterService;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parameter")
public class ParameterManagerController {

    private static Logger logger = LoggerFactory.getLogger(ParameterManagerController.class);

    @Autowired
    private IAlgorithmParameterService algorithmParameterService;

    @PostMapping("/group/{groupId}")
    public R<T> group(@PathVariable int groupId){
        AlgorithmParameter parameter = new AlgorithmParameter();
        parameter.setGroupId(groupId);
        return ResultUtil.success(algorithmParameterService.select(new QueryWrapper<AlgorithmParameter>(parameter)));
    }


//    /**
//     * 查找容器
//     */
//    @PostMapping("/search")
//    public R<T> search(@RequestBody ContainerRequest containerRequest){
//
//        PageInfoRequest pageInfoRequest = new PageInfoRequest();
//        BeanUtils.copyProperties(containerRequest,pageInfoRequest);
//        //
//        Example example = new Example(Container.class);
//        example.setOrderByClause("create_time desc");
//        Example.Criteria criteria = example.createCriteria();
//        if(containerRequest.getName() != null){
//            criteria.andLike("name", "%" + containerRequest.getName() + "%");
//        }
//        if(containerRequest.getStatus() != null){
//            criteria.andEqualTo("status",containerRequest.getStatus());
//        }
//        criteria.andEqualTo("isDelete",0);
//        PageInfo<Container> pageInfo = containerService.findByPage(pageInfoRequest,example);
//
//        setContainerMetrics(pageInfo.getList());
//
//        return ResultUtil.success(pageInfo);
//    }
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
//    /**
//     * 解析ContainerRequest参数
//     * @param containerRequest
//     * @return
//     */
    private Algorithm deserializeAlgorithmRequest(AlgorithmRequest algorithmRequest){
        Algorithm algorithm = new Algorithm();
        BeanUtils.copyProperties(algorithmRequest,algorithm);

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
