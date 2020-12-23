package com.zkwg.modelmanager.core.server;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zkwg.modelmanager.core.BaseContextHandler;
import com.zkwg.modelmanager.entity.Model;
import com.zkwg.modelmanager.entity.Server;
import com.zkwg.modelmanager.k8s.DeployServerThread;
import com.zkwg.modelmanager.k8s.K8sOperator;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.service.IContainerService;
import com.zkwg.modelmanager.service.IModelService;
import com.zkwg.modelmanager.service.IServerService;
import com.zkwg.modelmanager.utils.MD5Util;
import com.zkwg.modelmanager.ws.WebSocketServer;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

public class FlaskServerProcess extends ServerProcess {

    private static Logger logger = LoggerFactory.getLogger(FlaskServerProcess.class);

    private IModelService modelService;

    private IServerService serverService;

    private IContainerService containerService;

    private WebSocketServer webSocketServer;

    private K8sOperator k8sOperator;

    public FlaskServerProcess(IContainerService containerService, IModelService modelService, IServerService serverService,WebSocketServer webSocketServer,K8sOperator k8sOperator) {
        this.containerService = containerService;
        this.modelService = modelService;
        this.serverService = serverService;
        this.webSocketServer = webSocketServer;
        this.k8sOperator = k8sOperator;
    }


    @Override
    public Server create(Server server) {
        try {
            Server param = new Server(MD5Util.getMD5(server.getServerJson()));
            Server backServer = serverService.selectOne(new QueryWrapper<>(param));
            if(backServer == null){
                //TODO 有一致性问题，要重构
                String serverJson = server.getServerJson();
                String[] strArr = serverJson.split("###");
                Assert.notEmpty(strArr,"serverJson 不能为空");
                Assert.notNull(strArr[0],"deployment 部署文件不能为空");
                Assert.notNull(strArr[1],"service 部署文件不能为空");
                Deployment deployment = Serialization.unmarshal(strArr[0], Deployment.class);
                Service service = Serialization.unmarshal(strArr[1], Service.class);

                List<com.zkwg.modelmanager.entity.Container> backContainers =  getContainerList(deployment, (byte) 2);

                // 设置实例名称
                String oldServiceName = service.getMetadata().getName();
                final String deployInstanceName = deployment.getMetadata().getName() + "-" + UUID.randomUUID();
                final String serviceInstanceName = service.getMetadata().getName() + "-" + UUID.randomUUID();
                logger.info("生成服务实例名称: " + serviceInstanceName);
                deployment.getMetadata().setName(deployInstanceName);
                service.getMetadata().setName(serviceInstanceName);
                Map<String,String> annotationsMap = service.getMetadata().getAnnotations();
                String ambassadorValue = annotationsMap.get("getambassador.io/config");
                annotationsMap.put("getambassador.io/config", ambassadorValue.replaceAll(oldServiceName, serviceInstanceName));

                String newServerJson = Serialization.asJson(deployment) + "###" + Serialization.asJson(service);
                String deployInstanceJsonMd5 = MD5Util.getMD5(newServerJson);

                server.setIsDelete((byte) 0);
                server.setStatus((byte)7); // 创建
                server.setCreatetime(new Date());
                server.setServerJson(newServerJson);
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

        server.setStatus((byte) 8); // 部署中

        serverService.updateByPrimaryKeySelective(server);

        new DeployServerThread(BaseContextHandler.getTenant(), userId, server, k8sOperator, serverService, webSocketServer).run();
//        k8sOperator.seldonDeploy(userId, server,3, TimeUnit.MINUTES, (ServerServiceImpl) serverService);
    }

    @Override
    public R stop(K8sOperator k8sOperator, Server server) {
        String serverJson = server.getServerJson();
        String[] strArr = serverJson.split("###");
        // 解析对应资源
        Deployment deployment = Serialization.unmarshal(strArr[0], Deployment.class);
        Service service = Serialization.unmarshal(strArr[1], Service.class);
        try {
            k8sOperator.deleteDeployment(deployment);
            k8sOperator.deleteService(service);

            // 获取部署的模型和容器
            List<Model> models = getModelList(deployment, (byte) 1);
            List<com.zkwg.modelmanager.entity.Container> containers = getContainerList(deployment, (byte) 2);
            server.setStatus((byte)3);// 服务停止

            modelService.updateStatusByImage(models);
            containerService.updateStatusByNameEN(containers);
            serverService.updateByPrimaryKeySelective(server);

            return ResultUtil.success(server);
        } catch (Exception e) {

            throw new RuntimeException("删除服务出现异常！");
        }
    }

    @Override
    public String watchLog(Server server) {
        String serverJson = server.getServerJson();
        String[] strArr = serverJson.split("---");
        // 解析对应资源
        Deployment deployment = Serialization.unmarshal(strArr[0], Deployment.class);
        Pod pod = k8sOperator.getPod(deployment);
        ObjectMeta objectMeta = pod.getMetadata();
        Optional<Container> optional =  pod.getSpec().getContainers().stream().findFirst();
        Container container = optional.get();

        return k8sOperator.watchPodLog(objectMeta.getNamespace(),objectMeta.getName(),container.getName());
    }

    @Override
    public void delete(Server server) {
        server.setIsDelete((byte)1);
        serverService.updateByPrimaryKeySelective(server);

        String serverJson = server.getServerJson();
        String[] strArr = serverJson.split("---");
        // 解析对应资源
        Deployment deployment = Serialization.unmarshal(strArr[0], Deployment.class);
        // 获取关联的容器
//        SeldonDeployment seldonDeployment = Serialization.unmarshal(server.getServerJson(),SeldonDeployment.class);
        List<com.zkwg.modelmanager.entity.Container> containers = getContainerList(deployment, (byte) 1);

        // 通知修改容器状态    没有删除的服务中没有还分配这容器，设置容器为未分配
        for(com.zkwg.modelmanager.entity.Container container : containers){
//            Example example = new Example(Server.class);
//            example.createCriteria().andLike("serverJson", "%" + container.getNameEn() + "%")
//                    .andEqualTo("isDelete", 0);
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
        List<Model> models = getModelList(deployment, (byte) 1);
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
        String[] strArr = serverJson.split("###");
        // 解析对应资源
        Service service = Serialization.unmarshal(strArr[1], Service.class);
        ObjectMeta objectMeta = service.getMetadata();
        String v = "flask/" + objectMeta.getNamespace() + "/" + objectMeta.getName() + "/api/v1.0/predictions";
        return v;
    }

    private List<Model> getModelList(Deployment deployment, final byte status) {

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

    private List<com.zkwg.modelmanager.entity.Container> getContainerList(Deployment deployment, final byte status) {

        PodSpec podSpec = deployment.getSpec().getTemplate().getSpec();
        List<Container> containers = podSpec.getContainers();

        return containers.stream().map(c -> {
            com.zkwg.modelmanager.entity.Container con = new com.zkwg.modelmanager.entity.Container();
            con.setNameEn(c.getName());
            con.setStatus(status);
            return con;
        }).collect(Collectors.toList());
    }

}
