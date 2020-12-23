package com.zkwg.modelmanager.core.server;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zkwg.modelmanager.core.DeployResult;
import com.zkwg.modelmanager.core.IMessage;
import com.zkwg.modelmanager.entity.Model;
import com.zkwg.modelmanager.entity.Server;
import com.zkwg.modelmanager.entity.seldon.*;
import com.zkwg.modelmanager.k8s.K8sOperator;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.service.IContainerService;
import com.zkwg.modelmanager.service.IModelService;
import com.zkwg.modelmanager.service.IServerService;
import com.zkwg.modelmanager.service.impl.ServerServiceImpl;
import com.zkwg.modelmanager.utils.K8sClientUtils;
import com.zkwg.modelmanager.utils.MD5Util;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentStatus;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SeldonServerProcess extends ServerProcess {

    private static Logger logger = LoggerFactory.getLogger(SeldonServerProcess.class);

    private IModelService modelService;

    private IServerService serverService;

    private IContainerService containerService;

    private K8sOperator k8sOperator;

    public SeldonServerProcess(IContainerService containerService, IModelService modelService, IServerService serverService,K8sOperator k8sOperator) {
        this.containerService = containerService;
        this.modelService = modelService;
        this.serverService = serverService;
        this.k8sOperator = k8sOperator;
    }

    @Override
    public Server create(Server server) {
       try {
            Server param = new Server(server.getServerJsonMd5());
            Server backServer = serverService.selectOne(new QueryWrapper<>(param));
            if(backServer == null){
                //
                List<com.zkwg.modelmanager.entity.Container> backContainers = getContainers(server, (byte)2);
                SeldonDeployment seldonDeployment = Serialization.unmarshal(server.getServerJson(),SeldonDeployment.class);
                // 设置实例名称
                final String instanceName = seldonDeployment.getMetadata().getName() + "-" + UUID.randomUUID(); // 生成服务实例名称
                logger.info("生成服务实例名称: " + instanceName);
                seldonDeployment.getMetadata().setName(instanceName);
                //
                String deployInstanceJson = Serialization.asJson(seldonDeployment);
                String deployInstanceJsonMd5 = MD5Util.getMD5(deployInstanceJson);

                server.setIsDelete((byte) 0);
                server.setStatus((byte)7); // 创建
                server.setCreatetime(new Date());
                server.setServerJson(deployInstanceJson);
                server.setServerJsonMd5(deployInstanceJsonMd5);

                containerService.updateStatusByNameEN(backContainers);
                serverService.insertSelective(server);
            }
            return backServer == null ? server : backServer;
        } catch (Exception e) {
            throw new RuntimeException("创建服务失败！",e);
        }
    }

    @Override
    public void redeploy(String userId, K8sOperator k8sOperator, Server server) {

        Assert.state(StrUtil.isNotBlank(server.getServerJson()), "serverJson 为空！！！！");

        server.setStatus((byte) 8); // 部署中

        serverService.updateByPrimaryKeySelective(server);

        k8sOperator.seldonDeploy(userId, server,3, TimeUnit.MINUTES, (ServerServiceImpl) serverService);
    }

    @Override
    public R stop(K8sOperator k8sOperator, Server server) {

        SeldonDeployment seldonDeployment = Serialization.unmarshal(server.getServerJson(), SeldonDeployment.class);
        // 获取部署的模型和容器
        List<Model> models = getModelList(seldonDeployment);
        List<com.zkwg.modelmanager.entity.Container> containers = getContainerList(seldonDeployment);
        // 调用k8sOperator部署服务实例
        IMessage<DeployResult> message = k8sOperator.stop(seldonDeployment,2,TimeUnit.MINUTES);

        DeployResult deployResult = message.getMessage();
        SeldonDeployment seldonDeploymentResult = deployResult.getSeldonDeployment();


        switch (deployResult.getStatus()) {
            case STOP:
                models.stream().map(m -> {m.setStatus((byte) 1);return m;}).collect(Collectors.toList());
                modelService.updateStatusByUrl(models);
                containers.stream().map(m -> {m.setStatus((byte) 2);return m;}).collect(Collectors.toList());
                containerService.updateStatusByNameEN(containers);
                server.setStatus((byte)3);// 服务停止
                serverService.updateByPrimaryKeySelective(server);
                return ResultUtil.success(seldonDeploymentResult.getStatus());
            case TIME_OUT:
                logger.info("服务停止超时！！！");
//                models.stream().map(m -> {m.setStatus((byte) 4);return m;}).collect(Collectors.toList());
//                modelService.updateStatusByUrl(models);
//                containers.stream().map(m -> {m.setStatus((byte) 4);return m;}).collect(Collectors.toList());
//                containerService.updateStatusByNameEN(containers);
//                server.setStatus((byte) 6);
//                serverMapper.updateByPrimaryKeySelective(server);
                return ResultUtil.failure(seldonDeploymentResult.getStatus(),"服务停止超时！！！");
            case ERROR:
//                logger.info("服务停止异常！！！");
//                models.stream().map(m -> {m.setStatus((byte) 3);return m;}).collect(Collectors.toList());
//                modelService.updateStatusByUrl(models);
//                containers.stream().map(m -> {m.setStatus((byte) 5);return m;}).collect(Collectors.toList());
//                containerService.updateStatusByNameEN(containers);
//                //
//                server.setStatus((byte) 4);
//                serverMapper.updateByPrimaryKeySelective(server);
                return ResultUtil.failure(seldonDeploymentResult.getStatus(),"服务停止异常！！！");
        }
        return ResultUtil.success(server);
    }

    @Override
    public String watchLog(Server server) {
        SeldonDeployment seldonDeployment = Serialization.unmarshal(server.getRunResultJson(), SeldonDeployment.class);
        Deployment deployment = getRealDeployment(seldonDeployment);
        Pod pod = k8sOperator.getPod(deployment);

        String namespace = pod.getMetadata().getNamespace();
        String name = pod.getMetadata().getName();
        String container = getContainerName(seldonDeployment);

        return k8sOperator.watchPodLog(namespace,name,container);
    }

    @Override
    public void delete(Server server) {

        server.setIsDelete((byte)1);
        serverService.updateByPrimaryKeySelective(server);

        // 获取关联的容器
        SeldonDeployment seldonDeployment = Serialization.unmarshal(server.getServerJson(),SeldonDeployment.class);
        List<com.zkwg.modelmanager.entity.Container> containers = getContainerList(seldonDeployment);

        // 通知修改容器状态    没有删除的服务中没有还分配这容器，设置容器为未分配
        for(com.zkwg.modelmanager.entity.Container container : containers){
//            Example example = new Example(Server.class);
//            example.createCriteria().andLike("serverJson", "%" + container.getNameEn() + "%")
//                                    .andEqualTo("isDelete", 0);
//            List<Server> servers =  serverService.selectByExample(example);
            QueryWrapper<Server> queryWrapper = new QueryWrapper<Server>();
            queryWrapper.like("server_json", "%" + container.getNameEn() + "%").eq("is_delete", 0);
            List<Server> servers = serverService.select(queryWrapper);
            if(servers != null && servers.size() < 1){
                container.setStatus((byte) 1);
                container.setPodName("");
                containerService.updateByNameEN(container);
            }
        }

        // 获取关联的模型
        List<Model> models = getModelList(seldonDeployment);
        // 通知修改模型状态
        for(Model model : models){
//            Example example = new Example(Server.class);
//            example.createCriteria().andLike("serverJson", "%" + model.getMinioUrl() + "%")
//                    .andEqualTo("isDelete", 0);
//            List<Server> servers =  serverService.selectByExample(example);
            QueryWrapper<Server> queryWrapper = new QueryWrapper<Server>();
            queryWrapper.like("server_json", "%" + model.getMinioUrl() + "%").eq("is_delete", 0);
            List<Server> servers = serverService.select(queryWrapper);
            if(servers != null && servers.size() < 1){
                model.setStatus((byte) 1);
                modelService.updateStatusByUrl(model);
            }
        }

    }

    @Override
    public String test(Server server) {
        String serverJson = server.getServerJson();
        SeldonDeployment seldonDeployment = Serialization.unmarshal(serverJson,SeldonDeployment.class);
        ObjectMeta objectMeta = seldonDeployment.getMetadata();
        String v = "seldon/" + objectMeta.getNamespace() + "/" + objectMeta.getName() + "/api/v1.0/predictions";
        return v;
    }

    private String getContainerName(SeldonDeployment seldonDeployment) {
        PredictorSpec[] predictorSpecs = seldonDeployment.getSpec().getPredictors();
        PredictorSpec predictor = predictorSpecs[0];
        PredictiveUnit graph = predictor.getGraph();
        return graph.getName();
    }

    private Deployment getRealDeployment(SeldonDeployment seldonDeployment) {
        String namespace = seldonDeployment.getMetadata().getNamespace();
        Map<String, DeploymentStatus> deploymentStatus = seldonDeployment.getStatus().getDeploymentStatus();
        Assert.notEmpty(deploymentStatus,"不能获取服务的部署名称");
        String name = (String) deploymentStatus.keySet().toArray()[0];
        return  k8sOperator.getDeployment(namespace,name);
    }

    //TODO 这些以后抽到解析类，当工具使用  parse()
    private List<Model> getModelList(SeldonDeployment seldonDeployment) {
        List<String> models = new ArrayList<>();
        PredictorSpec[] predictorSpecs = seldonDeployment.getSpec().getPredictors();
        PredictiveUnit predictiveUnit = predictorSpecs[0].getGraph(); // 只获取第一个图
        Assert.notNull(predictiveUnit,"获取模型列表失败");

        models.add(predictiveUnit.getModelUri()); // 获取模型地址
        getChildern(predictiveUnit,models);

        Assert.notEmpty(models,"获取模型列表失败");
        return models.stream().map(s -> {
            Model model = new Model();
            model.setMinioUrl(s);
            return model;
        }).collect(Collectors.toList());
    }

    private void getChildern(PredictiveUnit predictiveUnit,List<String> models) {
        PredictiveUnit[] predictiveUnits = predictiveUnit.getChildren();
        if(predictiveUnits == null || predictiveUnits.length == 0){
            return;
        }
        for(PredictiveUnit p : predictiveUnits){
            models.add(p.getModelUri()); // 获取模型地址
            getChildern(p,models);
        }
    }

    private List<com.zkwg.modelmanager.entity.Container> getContainerList(SeldonDeployment seldonDeployment) {

        List<com.zkwg.modelmanager.entity.Container> containerList = new ArrayList<>();
        PredictorSpec[] predictorSpecs = seldonDeployment.getSpec().getPredictors();
        SeldonPodSpec[] seldonPodSpecs = predictorSpecs[0].getComponentSpecs();
        Assert.notEmpty(seldonPodSpecs,"获取容器列表失败");

        SeldonPodSpec specs = seldonPodSpecs[0];
        PodSpec podSpec = specs.getSpec();
        Assert.notNull(seldonPodSpecs,"获取容器列表失败");

        List<Container> containers = podSpec.getContainers();
        Assert.notEmpty(containers,"获取容器列表失败");

        return containers.stream().map(c -> {
            com.zkwg.modelmanager.entity.Container con = new com.zkwg.modelmanager.entity.Container();
            con.setNameEn(c.getName());
            return con;
        }).collect(Collectors.toList());
    }

    private List<com.zkwg.modelmanager.entity.Container> getContainers(Server server, byte status) {
        SeldonDeployment seldonDeployment = Serialization.unmarshal(server.getServerJson(),SeldonDeployment.class);

        SeldonDeploymentSpec seldonDeploymentSpec = seldonDeployment.getSpec();
        Assert.notNull(seldonDeploymentSpec,"serverJson中spec不能为空");

        PredictorSpec[] predictorSpecs = seldonDeploymentSpec.getPredictors();
        Assert.notEmpty(predictorSpecs,"serverJson中predictors不能为空");

        SeldonPodSpec[] seldonPodSpecs = predictorSpecs[0].getComponentSpecs();
        Assert.notEmpty(seldonPodSpecs,"serverJson中seldonPodSpecs不能为空");

        PodSpec podSpecs = seldonPodSpecs[0].getSpec();
        Assert.notNull(podSpecs,"serverJson中podSpecs不能为空");

        List<io.fabric8.kubernetes.api.model.Container> containers = podSpecs.getContainers();
        Assert.notEmpty(containers,"未分配容器");

        return containers.stream().map(c -> {
            com.zkwg.modelmanager.entity.Container con = new com.zkwg.modelmanager.entity.Container();
            con.setNameEn(c.getName());
            con.setStatus(status);
            return con;
        }).collect(Collectors.toList());
    }

}
