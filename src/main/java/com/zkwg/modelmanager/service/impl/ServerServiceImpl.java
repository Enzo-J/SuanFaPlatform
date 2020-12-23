package com.zkwg.modelmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zkwg.modelmanager.core.*;
import com.zkwg.modelmanager.core.server.ServerProcess;
import com.zkwg.modelmanager.core.server.ServerTypeEnum;
import com.zkwg.modelmanager.entity.Model;
import com.zkwg.modelmanager.entity.Server;
import com.zkwg.modelmanager.entity.seldon.PredictiveUnit;
import com.zkwg.modelmanager.entity.seldon.PredictorSpec;
import com.zkwg.modelmanager.entity.seldon.SeldonDeployment;
import com.zkwg.modelmanager.entity.seldon.SeldonPodSpec;
import com.zkwg.modelmanager.k8s.K8sOperator;
import com.zkwg.modelmanager.mapper.ServerMapper;
import com.zkwg.modelmanager.request.ServerRequest;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.security.AISecurityUser;
import com.zkwg.modelmanager.security.dataScope.DataScope;
import com.zkwg.modelmanager.service.IContainerService;
import com.zkwg.modelmanager.service.IModelService;
import com.zkwg.modelmanager.service.IServerService;
import com.zkwg.modelmanager.service.base.BaseService;
import com.zkwg.modelmanager.utils.K8sClientUtils;
import com.zkwg.modelmanager.utils.K8sProperties;
import com.zkwg.modelmanager.ws.WebSocketServer;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentStatus;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ServerServiceImpl extends BaseService<ServerMapper, Server> implements IServerService, Handler<IMessage<DeployResult>> {

    private static Logger logger = LoggerFactory.getLogger(ServerServiceImpl.class);

    @Autowired
    private K8sProperties k8sProperties;

    @Autowired
    private K8sOperator k8sOperator;

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private IModelService modelService;

    @Autowired
    private IContainerService containerService;

    private ServerMapper serverMapper;

    @Autowired
    public void setModelMapper(ServerMapper serverMapper) {
        this.serverMapper = serverMapper;
        this.baseMapper = serverMapper;
    }

//    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * 关闭服务实例
     * @param server
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R stop(Server server) throws Exception {
        ServerTypeEnum serverlTypeEnum = ServerTypeEnum.match(server.getImplementation());
        ServerProcess serverProcess = serverlTypeEnum.serverProcess;
        return serverProcess.stop(k8sOperator, server);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R redeploy(Server server) throws Exception {

        SeldonDeployment seldonDeployment = Serialization.unmarshal(server.getServerJson(), SeldonDeployment.class);
        // 获取部署的模型和容器
        List<Model> models = getModelList(seldonDeployment);
        List<com.zkwg.modelmanager.entity.Container> containers = getContainerList(seldonDeployment);
        // 调用k8sOperator部署服务实例
        IMessage<DeployResult> message = k8sOperator.deploy(seldonDeployment,3,TimeUnit.MINUTES);

        DeployResult deployResult = message.getMessage();
        SeldonDeployment seldonDeploymentResult = deployResult.getSeldonDeployment();
        switch (deployResult.getStatus()) {
            case RUNNING:
                Pod pod = getRunningPod(seldonDeploymentResult);
                models.stream().map(m -> {m.setStatus((byte) 2);return m;}).collect(Collectors.toList());
                modelService.updateStatusByUrl(models);
                containers.stream().map(m -> {
                    m.setStatus((byte) 3);
                    m.setNamespace(pod.getMetadata().getNamespace());
                    m.setPodName(pod.getMetadata().getName());
                    return m;}).collect(Collectors.toList());
                containerService.updateStatusByNameEN(containers);
                // 服务运行
                server.setStatus((byte)1);
                server.setPodJson(Serialization.asJson(pod));
                server.setRunResultJson(Serialization.asJson(seldonDeploymentResult));
//                serverMapper.updateByPrimaryKeySelective(server);
                serverMapper.updateById(server);
                return ResultUtil.success(seldonDeploymentResult.getStatus());
            case TIME_OUT:
                logger.info("服务运行超时！！！");
                models.stream().map(m -> {m.setStatus((byte) 4);return m;}).collect(Collectors.toList());
                modelService.updateStatusByUrl(models);
                containers.stream().map(m -> {m.setStatus((byte) 4);return m;}).collect(Collectors.toList());
                containerService.updateStatusByNameEN(containers);
                server.setStatus((byte) 6);
//                serverMapper.updateByPrimaryKeySelective(server);
                serverMapper.updateById(server);
                return ResultUtil.failure(seldonDeploymentResult.getStatus(),"服务运行超时！！！");
            case ERROR:
                logger.info("服务运行异常！！！");
                models.stream().map(m -> {m.setStatus((byte) 3);return m;}).collect(Collectors.toList());
                modelService.updateStatusByUrl(models);
                containers.stream().map(m -> {m.setStatus((byte) 5);return m;}).collect(Collectors.toList());
                containerService.updateStatusByNameEN(containers);
                //
                server.setStatus((byte) 4);
//                serverMapper.updateByPrimaryKeySelective(server);
                serverMapper.updateById(server);
                return ResultUtil.failure(seldonDeploymentResult.getStatus(),"服务运行异常！！！");
        }
        return ResultUtil.success();
    }

    private Pod getRunningPod(SeldonDeployment seldonDeploymentResult){
        Map<String, DeploymentStatus> deploymentStatus = seldonDeploymentResult.getStatus().getDeploymentStatus();
        Assert.notEmpty(deploymentStatus,"无法获取deploymentStatus！");
        Object[] deployNameArr = deploymentStatus.keySet().toArray();
        Deployment realDeployment = K8sClientUtils.getInstance(k8sProperties).getDeployment(seldonDeploymentResult.getMetadata().getNamespace(),deployNameArr[0].toString());
        return K8sClientUtils.getInstance(k8sProperties).getPod(realDeployment);
    }

    private List<com.zkwg.modelmanager.entity.Container> getContainerList(Deployment deployment, byte status) {

        PodSpec podSpec = deployment.getSpec().getTemplate().getSpec();
        List<Container> containers = podSpec.getContainers();

        return containers.stream().map(c -> {
            com.zkwg.modelmanager.entity.Container con = new com.zkwg.modelmanager.entity.Container();
            con.setNameEn(c.getName());
            con.setStatus(status);
            return con;
        }).collect(Collectors.toList());
    }

    private List<com.zkwg.modelmanager.entity.Container> getContainerList(Deployment deployment) {

        PodSpec podSpec = deployment.getSpec().getTemplate().getSpec();
        List<Container> containers = podSpec.getContainers();

        return containers.stream().map(c -> {
            com.zkwg.modelmanager.entity.Container con = new com.zkwg.modelmanager.entity.Container();
            con.setNameEn(c.getName());
            return con;
        }).collect(Collectors.toList());
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

    private List<Model> getModelList(Deployment deployment, byte status) {

        PodSpec podSpec = deployment.getSpec().getTemplate().getSpec();
        List<Container> containers = podSpec.getContainers();
        //
        return containers.stream().map(s -> {
            Model model = new Model();
            model.setImage(s.getImage());
            model.setStatus(status);
            return model;
        }).collect(Collectors.toList());
    }

    private List<Model> getModelList(Deployment deployment) {

        PodSpec podSpec = deployment.getSpec().getTemplate().getSpec();
        List<Container> containers = podSpec.getContainers();
        //
        return containers.stream().map(s -> {
            Model model = new Model();
            model.setImage(s.getImage());
            return model;
        }).collect(Collectors.toList());
    }


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

    //TODO 这里查看单模型服务的日志，以后要改
    @Override
    public String watchLog(Server server) {

//        SeldonDeployment seldonDeployment = Serialization.unmarshal(server.getRunResultJson(), SeldonDeployment.class);
//        Deployment deployment = getRealDeployment(seldonDeployment);
//        Pod pod = getPod(deployment);
//
//        String namespace = pod.getMetadata().getNamespace();
//        String name = pod.getMetadata().getName();
//        String container = getContainerName(seldonDeployment);
//        String log = K8sClientUtils.getInstance(k8sProperties).watchPodLog(namespace,name,container);

        ServerTypeEnum serverlTypeEnum = ServerTypeEnum.match(server.getImplementation());
        ServerProcess serverProcess = serverlTypeEnum.serverProcess;
        return serverProcess.watchLog(server);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Server create(Server server) {

        ServerTypeEnum serverlTypeEnum = ServerTypeEnum.match(server.getImplementation());
        ServerProcess serverProcess = serverlTypeEnum.serverProcess;
        return serverProcess.create(server);
//        try {
//            Server param = new Server(server.getServerJsonMd5());
//            Server backServer = serverMapper.selectOne(param);
//            if(backServer == null){
//                //
//                List<Container> containers = getContainers(server);
//                List<com.zkwg.modelmanager.entity.Container> backContainers = containers.stream().map( c -> {
//                    com.zkwg.modelmanager.entity.Container back = new com.zkwg.modelmanager.entity.Container();
//                    back.setStatus((byte)2);  // 已分配
//                    back.setNameEn(c.getName());
//                    return back;
//                }).collect(Collectors.toList());
//                containerService.updateStatusByNameEN(backContainers);
//                serverMapper.insertSelective(server);
//            }
//            return backServer == null ? server : backServer;
//        } catch (Exception e) {
//            throw new RuntimeException("创建服务失败！",e);
//        }
    }

    @Override
    public void redeploy(Server server, String userId) {

        ServerTypeEnum serverlTypeEnum = ServerTypeEnum.match(server.getImplementation());

        ServerProcess serverProcess = serverlTypeEnum.serverProcess;

        serverProcess.redeploy(userId, k8sOperator, server);

//        server.setStatus((byte) 8); // 部署中
//
//        serverMapper.updateByPrimaryKey(server);
//
//        k8sOperator.seldonDeploy(userId,server,3,TimeUnit.MINUTES, this);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteServer(Server server) {
        ServerTypeEnum serverlTypeEnum = ServerTypeEnum.match(server.getImplementation());
        ServerProcess serverProcess = serverlTypeEnum.serverProcess;
        serverProcess.delete(server);

    }

    @Override
    public void deploySuccess(IMessage<K8sDeployResult> message) {
        logger.info("服务运行成功！！！");
        K8sDeployResult deployResult = message.getMessage();
        String userId = deployResult.getUserId();
        Server server = deployResult.getServer();
        String deployResultStr = deployResult.getDeployResultStr();

        Deployment deployment = Serialization.unmarshal(deployResultStr, Deployment.class);
//        PodTemplateSpec pod = deployment.getSpec().getTemplate();
        Pod runPod = k8sOperator.getPod(deployment);
        // 获取部署的模型和容器
        List<Model> models = getModelList(deployment);
        List<com.zkwg.modelmanager.entity.Container> containers = getContainerList(deployment);
        models.stream().map(m -> {m.setStatus((byte) 2);return m;}).collect(Collectors.toList());
        modelService.updateStatusByImage(models);
        containers.stream().map(m -> {
            m.setStatus((byte) 3);
            m.setNamespace(runPod.getMetadata().getNamespace());
            m.setPodName(runPod.getMetadata().getName());
            return m;}).collect(Collectors.toList());
        containerService.updateStatusByNameEN(containers);
        // 服务运行
        server.setStatus((byte)1);
        server.setPodJson(Serialization.asJson(runPod));
        server.setRunResultJson(deployResultStr);
//        serverMapper.updateByPrimaryKeySelective(server);
        serverMapper.updateById(server);
        webSocketServer.sendInfo("Success",userId);
    }

    @Override
    public void deployError(IMessage<K8sDeployResult> message) {
        logger.info("服务运行异常！！！");
        K8sDeployResult deployResult = message.getMessage();
        String userId = deployResult.getUserId();
        Server server = deployResult.getServer();
        String deployResultStr = deployResult.getDeployResultStr();

        Deployment deployment = Serialization.unmarshal(deployResultStr, Deployment.class);
        // 获取部署的模型和容器
        List<Model> models = getModelList(deployment);
        List<com.zkwg.modelmanager.entity.Container> containers = getContainerList(deployment);

        models.stream().map(m -> {m.setStatus((byte) 3);return m;}).collect(Collectors.toList());
        modelService.updateStatusByUrl(models);
        containers.stream().map(m -> {m.setStatus((byte) 5);return m;}).collect(Collectors.toList());
        containerService.updateStatusByNameEN(containers);
        //
        server.setStatus((byte) 4);
//        serverMapper.updateByPrimaryKeySelective(server);
        serverMapper.updateById(server);
        webSocketServer.sendInfo("Error",userId);
    }

    @Override
    public void deployTimeout(IMessage<K8sDeployResult> message) {
        logger.info("服务运行超时！！！");
        K8sDeployResult deployResult = message.getMessage();
        String userId = deployResult.getUserId();
        Server server = deployResult.getServer();

        String serverJson = server.getServerJson();
        String[] strArr = serverJson.split("---");
        Assert.notEmpty(strArr,"serverJson 不能为空");
        Assert.notNull(strArr[0],"deployment 部署文件不能为空");

        Deployment deployment = Serialization.unmarshal(strArr[0], Deployment.class);

        // 获取部署的模型和容器
        List<Model> models = getModelList(deployment, (byte) 4);
        List<com.zkwg.modelmanager.entity.Container> containers = getContainerList(deployment, (byte) 4);

//        models.stream().map(m -> {m.setStatus((byte) 4);return m;}).collect(Collectors.toList());
//        containers.stream().map(m -> {m.setStatus((byte) 4);return m;}).collect(Collectors.toList());

        server.setStatus((byte) 6);

        modelService.updateStatusByUrl(models);
        containerService.updateStatusByNameEN(containers);
//        serverMapper.updateByPrimaryKeySelective(server);
        serverMapper.updateById(server);
        webSocketServer.sendInfo("TimeOut",userId);
    }

    @Override
    public IPage<Server> subscribeList(Integer pageNum, Integer pageSize, ServerRequest serverRequest) {
        return serverMapper.subscribeList(new Page(pageNum, pageSize), serverRequest, new DataScope());
    }

    @Override
    public IPage<Server> publicServerList(Integer pageNum, Integer pageSize, ServerRequest serverRequest) {
        return serverMapper.publicServerList(new Page(pageNum, pageSize), serverRequest);
    }

    @Override
    public IPage<Server> findPage(int pageNum, int pageSize, Wrapper<Server> wrapper) {
        return serverMapper.findPage(new Page(pageNum, pageSize), wrapper, new DataScope());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlerMessage(IMessage<DeployResult> message){

        DeployResult deployResult = message.getMessage();
        String userId = deployResult.getUserId();
        Server server = deployResult.getServer();
        WebSocketServer webSocketServer = deployResult.getWebSocketServer();
        SeldonDeployment seldonDeploymentResult = deployResult.getSeldonDeployment();
        // 获取部署的模型和容器
        List<Model> models = getModelList(seldonDeploymentResult);
        List<com.zkwg.modelmanager.entity.Container> containers = getContainerList(seldonDeploymentResult);

        try {
            switch (deployResult.getStatus()) {
                case RUNNING:
                    logger.info("服务运行成功！！！");
                    Pod pod = getRunningPod(seldonDeploymentResult);
                    models.stream().map(m -> {m.setStatus((byte) 2);return m;}).collect(Collectors.toList());
                    modelService.updateStatusByUrl(models);
                    containers.stream().map(m -> {
                        m.setStatus((byte) 3);
                        m.setNamespace(pod.getMetadata().getNamespace());
                        m.setPodName(pod.getMetadata().getName());
                        return m;}).collect(Collectors.toList());
                    containerService.updateStatusByNameEN(containers);
                    // 服务运行
                    server.setStatus((byte)1);
                    server.setPodJson(Serialization.asJson(pod));
                    server.setRunResultJson(Serialization.asJson(seldonDeploymentResult));
//                serverMapper.updateByPrimaryKeySelective(server);
                    serverMapper.updateById(server);
                    webSocketServer.sendInfo("Success",userId);
                    break;
                case TIME_OUT:
                    logger.info("服务运行超时！！！");
                    models.stream().map(m -> {m.setStatus((byte) 4);return m;}).collect(Collectors.toList());
                    modelService.updateStatusByUrl(models);
                    containers.stream().map(m -> {m.setStatus((byte) 4);return m;}).collect(Collectors.toList());
                    containerService.updateStatusByNameEN(containers);
                    server.setStatus((byte) 6);
//                serverMapper.updateByPrimaryKeySelective(server);
                    serverMapper.updateById(server);
                    webSocketServer.sendInfo("TimeOut",userId);
                    break;
                case ERROR:
                    logger.info("服务运行异常！！！");
                    models.stream().map(m -> {m.setStatus((byte) 3);return m;}).collect(Collectors.toList());
                    modelService.updateStatusByUrl(models);
                    containers.stream().map(m -> {m.setStatus((byte) 5);return m;}).collect(Collectors.toList());
                    containerService.updateStatusByNameEN(containers);
                    //
                    server.setStatus((byte) 4);
//                serverMapper.updateByPrimaryKeySelective(server);
                    serverMapper.updateById(server);
                    webSocketServer.sendInfo("Failed",userId);
                    break;
            }
        } catch (Exception e) {
            logger.error("更新服务状态异常 ", e);
            throw new RuntimeException("更新服务状态异常");
        }
    }

//    private List<Container> getContainers(Server server) {
//        SeldonDeployment seldonDeployment = Serialization.unmarshal(server.getServerJson(),SeldonDeployment.class);
//
//        SeldonDeploymentSpec seldonDeploymentSpec = seldonDeployment.getSpec();
//        Assert.notNull(seldonDeploymentSpec,"serverJson中spec不能为空");
//
//        PredictorSpec[] predictorSpecs = seldonDeploymentSpec.getPredictors();
//        Assert.notEmpty(predictorSpecs,"serverJson中predictors不能为空");
//
//        SeldonPodSpec[] seldonPodSpecs = predictorSpecs[0].getComponentSpecs();
//        Assert.notEmpty(seldonPodSpecs,"serverJson中seldonPodSpecs不能为空");
//
//        PodSpec podSpecs = seldonPodSpecs[0].getSpec();
//        Assert.notNull(podSpecs,"serverJson中podSpecs不能为空");
//
//        List<Container> containers = podSpecs.getContainers();
//        Assert.notEmpty(containers,"未分配容器");
//        return containers;
//    }

    private String getContainerName(SeldonDeployment seldonDeployment) {
        PredictorSpec[] predictorSpecs = seldonDeployment.getSpec().getPredictors();
        PredictorSpec predictor = predictorSpecs[0];
        PredictiveUnit graph = predictor.getGraph();
        return graph.getName();
    }

    private Pod getPod(Deployment deployment) {
        return K8sClientUtils.getInstance(k8sProperties).getPod(deployment);
    }

    private Deployment getRealDeployment(SeldonDeployment seldonDeployment) {
        String namespace = seldonDeployment.getMetadata().getNamespace();
        Map<String, DeploymentStatus> deploymentStatus = seldonDeployment.getStatus().getDeploymentStatus();
        Assert.notEmpty(deploymentStatus,"不能获取服务的部署名称");
        String name = (String) deploymentStatus.keySet().toArray()[0];
        return K8sClientUtils.getInstance(k8sProperties).getDeployment(namespace,name);
    }

}
