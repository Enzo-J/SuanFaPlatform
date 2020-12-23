package com.zkwg.modelmanager.service.impl;

import com.zkwg.modelmanager.entity.DeployProcessDef;
import com.zkwg.modelmanager.entity.Server;
import com.zkwg.modelmanager.entity.seldon.SeldonDeployment;
import com.zkwg.modelmanager.entity.seldon.SeldonDeploymentStatus;
import com.zkwg.modelmanager.exception.type.DeployProcessDefExceptionEnum;
import com.zkwg.modelmanager.mapper.DeployProcessDefMapper;
import com.zkwg.modelmanager.mapper.ServerMapper;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.service.IContainerService;
import com.zkwg.modelmanager.service.IDeployProcessDefService;
import com.zkwg.modelmanager.service.base.BaseService;
import com.zkwg.modelmanager.utils.K8sClientUtils;
import com.zkwg.modelmanager.utils.K8sProperties;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentStatus;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class DeployProcessDefServiceImpl extends BaseService<DeployProcessDefMapper, DeployProcessDef> implements IDeployProcessDefService {

    private static Logger logger = LoggerFactory.getLogger(DeployProcessDefServiceImpl.class);

    @Autowired
    private K8sProperties k8sProperties;

    @Autowired
    private ServerMapper serverMapper;

    @Autowired
    private IContainerService containerService;

    private DeployProcessDefMapper deployProcessDefMapper;

    @Autowired
    public void setModelMapper(DeployProcessDefMapper deployProcessDefMapper) {
        this.deployProcessDefMapper = deployProcessDefMapper;
        this.baseMapper = deployProcessDefMapper;
    }

    /**
     * 运行(生成部署实例/服务实例)
     * @param id
     * @return
     */
    @Override
    public R run(int id) throws Exception {

        DeployProcessDef deployProcessDef = deployProcessDefMapper.selectById(id);
        DeployProcessDefExceptionEnum.DeployProcessDef_Not_Exsist.assertNotNull(deployProcessDef);

        final CountDownLatch latch = new CountDownLatch(1);
        final SeldonDeployment seldonDeploymentResult = new SeldonDeployment();

        final SeldonDeployment deployment = Serialization.unmarshal(deployProcessDef.getDeployJson(), SeldonDeployment.class);
        final String namespace = deployment.getMetadata().getNamespace();
        final String instanceName = deployment.getMetadata().getName() + "-" + UUID.randomUUID(); // 生成服务实例名称
        logger.info("生成服务实例名称: " + instanceName);
        deployment.getMetadata().setName(instanceName);
        final String deployInstanceJson = Serialization.asJson(deployment);
        logger.info("服务实例JSON: {}"+deployInstanceJson);

        CreateCRDWatcher watcher = new DeployProcessDefServiceImpl.CreateCRDWatcher(instanceName,seldonDeploymentResult,latch);
        Watch watch = K8sClientUtils.getInstance(k8sProperties).createOrReplaceCRD(namespace,instanceName,deployInstanceJson, watcher);

        latch.await(3, TimeUnit.MINUTES);

        // 创建部署流程的对应部署服务实例
        Server server = createServerResult(deployProcessDef,deployInstanceJson,seldonDeploymentResult);
        updateContainerStatus(server);
        watch.close();
        return ResultUtil.success(seldonDeploymentResult.getStatus());
    }

    private Server createServerResult(DeployProcessDef deployProcessDef,String deployInstanceJson, SeldonDeployment seldonDeploymentResult) {

        byte status = getRunStatus(seldonDeploymentResult);
        Server server = new Server();
        server.setName(deployProcessDef.getName());
        server.setStatus(status); // 服务实例运行
        server.setServerJson(deployInstanceJson);
        server.setRunResultJson(Serialization.asJson(seldonDeploymentResult));
        server.setCreatetime(new Date());
        server.setProcessDefId(deployProcessDef.getId());

        Pod pod = null;
        try {
            Map<String, DeploymentStatus> deploymentStatus = seldonDeploymentResult.getStatus().getDeploymentStatus();
            if(deploymentStatus != null){
                Object[] deployNameArr = deploymentStatus.keySet().toArray();
                Deployment realDeployment = K8sClientUtils.getInstance(k8sProperties).getDeployment(seldonDeploymentResult.getMetadata().getNamespace(),deployNameArr[0].toString());
                pod = K8sClientUtils.getInstance(k8sProperties).getPod(realDeployment);
            }
        } catch (Exception e) {
            logger.error("获取运行中的Pod出现异常", e);
        }

        server.setPodJson(pod == null ? "" : Serialization.asJson(pod));
//        serverMapper.insertSelective(server);
        serverMapper.insert(server);
        return server;
    }

    private void updateContainerStatus(Server server) {
        String podJson = server.getPodJson();
        if(!"".equals(podJson)){
            Pod pod = Serialization.unmarshal(podJson,Pod.class);
            String namespace = pod.getMetadata().getNamespace();
            String podName = pod.getMetadata().getName();
            List<Container> containers = pod.getSpec().getContainers();
            for(Container container : containers){
                com.zkwg.modelmanager.entity.Container param = new com.zkwg.modelmanager.entity.Container();
                param.setNameEn(container.getName());
                param.setRunJson(Serialization.asJson(container));
                param.setPodName(podName);
                param.setStatus((byte)3);
                param.setNamespace(namespace);
                containerService.updateByNameEN(param);
            }
        }
    }

    private byte getRunStatus(SeldonDeployment seldonDeploymentResult) {
        if(seldonDeploymentResult.getStatus() == null){
            return  6; // 超时
        }
        if(seldonDeploymentResult.getStatus() != null && "Available".equals(seldonDeploymentResult.getStatus().getState())){
            return  1; // 运行中
        }
        return 4;// 运行失败
    }

    @Override
    public void insertAndGetId(DeployProcessDef deployProcessDef) {
        deployProcessDefMapper.insertAndGetId(deployProcessDef);
    }

    private class CreateCRDWatcher implements Watcher<String> {

        private String instanceName;

        private SeldonDeployment seldonDeploymentResult;

        private CountDownLatch latch;

        public CreateCRDWatcher(String instanceName, SeldonDeployment seldonDeploymentResult, CountDownLatch latch ) {
            this.instanceName = instanceName;
            this.seldonDeploymentResult = seldonDeploymentResult;
            this.latch = latch;
        }

        @Override
        public void eventReceived(Watcher.Action action, String reource) {
            logger.info("{}: {}", action, reource);
            SeldonDeployment seldonDeployment = Serialization.unmarshal(reource,SeldonDeployment.class);
            SeldonDeploymentStatus status = seldonDeployment.getStatus();
            ObjectMeta meta = seldonDeployment.getMetadata();
            if(status != null && meta != null && "Available".equals(status.getState()) && instanceName.equals(meta.getName())){
                BeanUtils.copyProperties(seldonDeployment, seldonDeploymentResult);
                latch.countDown();
            }
        }

        @Override
        public void onClose(KubernetesClientException e) {
            logger.debug("Watcher onClose");
            if (e != null) {
                logger.error(e.getMessage(), e);
            }
        }


    }

}
