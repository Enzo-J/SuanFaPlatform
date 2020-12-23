package com.zkwg.modelmanager.controller;

import com.zkwg.modelmanager.entity.ResourcePool;
import com.zkwg.modelmanager.exception.type.ResourcePoolExceptionEnum;
import com.zkwg.modelmanager.request.PageInfoRequest;
import com.zkwg.modelmanager.request.ResourcePoolRequest;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.service.IResourcePoolService;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.utils.Serialization;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "资源池模块")
@RestController
@RequestMapping("/resourcePool")
public class ResourcePoolController {

    private static Logger logger = LoggerFactory.getLogger(ResourcePoolController.class);

    @Value("${grafana.url}")
    private String endpoint;

    @Autowired
    private IResourcePoolService resourcePoolService;

    /**
     * 保存资源池
     */
    @ApiOperation(value = "创建资源池")
    @PostMapping("/save")
    public R<T> save(@Validated({ResourcePoolRequest.SaveResourcePool.class})
                     @RequestBody  ResourcePoolRequest resourcePoolRequest) {
        ResourcePool resourcePool = deserializeResourcePool(resourcePoolRequest);
        resourcePoolService.insertSelective(resourcePool);
        return ResultUtil.success();
    }

    /**
     * 查看资源池列表
     */
    @ApiOperation(value = "查看资源池列表")
    @PostMapping("/search")
    public R<T> search(@Validated({ResourcePoolRequest.ListResourcePool.class})
                    @RequestBody ResourcePoolRequest resourcePoolRequest){

        PageInfoRequest pageInfoRequest = new PageInfoRequest();
        BeanUtils.copyProperties(resourcePoolRequest,pageInfoRequest);
        //
//        Example example = new Example(ResourcePool.class);
//        example.createCriteria()
//                .andLike("name","%"+resourcePoolRequest.getName()+"%");
////                .andEqualTo("is_delete",0);
//
//        PageInfo<ResourcePool> pageInfo = resourcePoolService.findByPage(pageInfoRequest,example);
//        return ResultUtil.success(pageInfo);
        return null;
    }

    /**
     * 查看单个资源池
     */
    @ApiOperation(value = "查看资源池详情")
    @GetMapping("/list/{id:[0-9]*}")
    public R<T> listOne(@PathVariable("id") int id){
        ResourcePool rp = resourcePoolService.selectByPrimaryKey(id);
        return ResultUtil.success(rp);
    }

    /**
     * 修改资源池
     */
    @ApiOperation(value = "修改资源池详情")
    @PostMapping("/update/{id:[0-9]*}")
    public R<T> update(@PathVariable("id") int id,@Validated({ResourcePoolRequest.SaveResourcePool.class})
                       @RequestBody  ResourcePoolRequest resourcePoolRequest){

        checkResourcePoolStatus(id,4);
        //修改资源池信息
        ResourcePool resourcePool = deserializeResourcePool(resourcePoolRequest);
        resourcePool.setId(id);
        resourcePoolService.updateByPrimaryKeySelective(resourcePool);
        return ResultUtil.success();
    }

    /**
     * 性能检测
     */
    @ApiOperation(value = "资源池性能检测")
    @GetMapping("/monitor/{id:[0-9]*}")
    public R<T> monitor(@PathVariable("id") int id){

        checkResourcePoolStatus(id,1);
        //TODO 使用k8s java client 获取pod运行信息;或跳转到Grafana页面

        Map<String,String> map = new HashMap<>();
        map.put("url",endpoint+"/d/6581e46e4e5c7ba40a07646395ef7b23/kubernetes-compute-resources-pod?orgId=1&refresh=10s&var-datasource=default&var-cluster=&var-namespace=default&var-pod=ambassador-7c66b69b5d-72mgj");

        return ResultUtil.success(map);
    }

    /**
     * 根据状态判定操作是否可以执行
     * @param id 资源池ID
     * @param operator 检测操作：1.性能检测 2.删除 3.资源分配 4.修改
     */
    private void checkResourcePoolStatus(int id,int operator) {
        ResourcePool rp = resourcePoolService.selectByPrimaryKey(id);
        if(operator == 1 && rp.getStatus() != 1){ // 未运行,不能进行性能检测
            ResourcePoolExceptionEnum.Resource_Pool_Not_Running.throwException();
        }
        if(operator == 4 && rp.getStatus() == 1){ // 运行中,不能进行修改
            ResourcePoolExceptionEnum.Resource_Pool_Is_Running.throwException();
        }
    }

    /**
     * 解析resourcePoolRequest参数
     * @param resourcePoolRequest
     * @return
     */
    private ResourcePool deserializeResourcePool(ResourcePoolRequest resourcePoolRequest){

        Pod pod = new Pod();
        ResourcePool resourcePool = new ResourcePool();

        BeanUtils.copyProperties(resourcePoolRequest,pod);
        BeanUtils.copyProperties(resourcePoolRequest,resourcePool);

        String podJson = Serialization.asJson(pod);
        logger.debug(podJson);

        resourcePool.setPodJson(podJson);
        resourcePool.setStatus(1);
        resourcePool.setCreateTime(new Date());

        return resourcePool;
    }


}
