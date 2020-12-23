package com.zkwg.modelmanager.utils;

import com.zkwg.modelmanager.ModelManagerApplication;
import com.zkwg.modelmanager.controller.ModelManagerController;
import com.zkwg.modelmanager.entity.Server;
import com.zkwg.modelmanager.mapper.ServerMapper;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelManagerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class K8sClientUtilsTest {


    private static Logger logger = LoggerFactory.getLogger(K8sClientUtilsTest.class);

    @Autowired
    public K8sProperties k8sProperties;

    @Autowired
    private ServerMapper serverMapper;

    @Test
    void createCRD() throws InterruptedException {

        String namespace = "sklearn-model";

        final String name = "sklearn-iris";

        String seldonDeployJson = "{\n" +
                "    \"apiVersion\":\"machinelearning.seldon.io/v1\",\n" +
                "    \"kind\":\"SeldonDeployment\",\n" +
                "    \"metadata\":{\n" +
                "        \"name\":\"sklearn-iris\",\n" +
                "        \"namespace\":\"sklearn-model\"\n" +
                "    },\n" +
                "    \"spec\":{\n" +
                "        \"predictors\":[\n" +
                "            {\n" +
                "                \"replicas\":2,\n" +
                "                \"name\":\"default\",\n" +
                "                \"componentSpecs\":null,\n" +
                "                \"graph\":{\n" +
                "                    \"implementation\":\"SKLEARN_SERVER\",\n" +
                "                    \"name\":\"classifier\",\n" +
                "                    \"modelUri\":\"s3://sklearn-server/2020-06-30--18:07:49\",\n" +
                "                    \"envSecretRefName\":\"seldon-init-container-secret\"\n" +
                "                }\n" +
                "            }\n" +
                "        ],\n" +
                "        \"name\":\"iris\",\n" +
                "        \"annotations\":{\n" +
                "            \"seldon.io/executor\":\"true\"\n" +
                "        }\n" +
                "    }\n" +
                "}";

        final CountDownLatch latch = new CountDownLatch(1);

        Watch watch = K8sClientUtils.getInstance(k8sProperties).createOrReplaceCRD(namespace,name,seldonDeployJson, new Watcher<String>() {
            @Override
            public void eventReceived(Watcher.Action action, String resource) {
                logger.info("{}: {}", action, resource);
                Map<String, Object> map = Serialization.unmarshal(resource,Map.class);
                Map<String, Object> status = (Map<String, Object>) map.get("status");
                Map<String, Object> metadata = (Map<String, Object>) map.get("metadata");
                if(metadata != null && status != null && "Available".equals(status.get("state")) && name.equals(metadata.get("name"))){
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
        });

        logger.info("部署中。。。");

        latch.await(1, TimeUnit.MINUTES);

        watch.close();

        logger.info("部署完成");

    }


    @Test
    void deleteCRD() throws InterruptedException {

        String namespace = "sklearn-model";

        String name = "sklearn-iris";

        final CountDownLatch latch = new CountDownLatch(1);

        Watch watch = K8sClientUtils.getInstance(k8sProperties).deleteCRD(namespace,name, new Watcher<String>() {
            @Override
            public void eventReceived(Watcher.Action action, String resource) {
                logger.info("{}: {}", action, resource);
            }

            @Override
            public void onClose(KubernetesClientException e) {
                logger.debug("Watcher onClose");
                if (e != null) {
                    logger.error(e.getMessage(), e);
                }
            }
        });

        logger.info("删除中。。。");

        latch.await(1, TimeUnit.MINUTES);

        watch.close();

        logger.info("完成");

    }

    @Test
    public void watchPodLog(){
        Server server = serverMapper.selectById(5);
        Deployment seldonDeployment = Serialization.unmarshal(server.getRunResultJson(), Deployment.class);
        Deployment deployment = getRealDeployment(seldonDeployment);
        Pod pod = getPod(deployment);
        String namespace = pod.getMetadata().getNamespace();
        String name = pod.getMetadata().getName();
        String container = getContainerName(seldonDeployment);
        String log = K8sClientUtils.getInstance(k8sProperties).watchPodLog(namespace,name,container);

        logger.info(log);
    }

    private String getContainerName(Deployment seldonDeployment) {
        Map<String,Object> additionalProperties = seldonDeployment.getSpec().getAdditionalProperties();
        List<Map<String,Object>> predictors = (List<Map<String,Object>>) additionalProperties.get("predictors");
        Map<String,Object> predictor = predictors.get(0);
        Map<String,Object> graph = (Map<String,Object>) predictor.get("graph");
        return graph.get("name").toString();
    }

    private Pod getPod(Deployment deployment) {
        return K8sClientUtils.getInstance(k8sProperties).getPod(deployment);
    }

    private Deployment getRealDeployment(Deployment seldonDeployment) {
        String namespace = seldonDeployment.getMetadata().getNamespace();
        Map<String,Object> additionalProperties = seldonDeployment.getStatus().getAdditionalProperties();
        Map<String,Object> deploymentStatus = (Map<String, Object>) additionalProperties.get("deploymentStatus");
        if(deploymentStatus == null || deploymentStatus.isEmpty()){
            throw new RuntimeException("不能获取服务的部署名称");
        }
        String name = (String) deploymentStatus.keySet().toArray()[0];
        return K8sClientUtils.getInstance(k8sProperties).getDeployment(namespace,name);
    }


}