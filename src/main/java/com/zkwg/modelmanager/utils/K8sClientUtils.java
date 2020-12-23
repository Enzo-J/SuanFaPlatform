package com.zkwg.modelmanager.utils;

import com.google.common.collect.Maps;
import com.zkwg.modelmanager.entity.Container;
import com.zkwg.modelmanager.entity.seldon.SeldonDeployment;
import com.zkwg.modelmanager.exception.type.K8sClientExceptionEnum;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.api.model.metrics.v1beta1.ContainerMetrics;
import io.fabric8.kubernetes.api.model.metrics.v1beta1.NodeMetricsList;
import io.fabric8.kubernetes.api.model.metrics.v1beta1.PodMetrics;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class K8sClientUtils {

    private static Logger logger = LoggerFactory.getLogger(K8sClientUtils.class);

    private K8sProperties k8sProperties;

    private static K8sClientUtils k8sClientUtils;

    private static CustomResourceDefinitionContext crdContext;

    private final static ThreadLocal<KubernetesClient> threadLocal = new ThreadLocal<>();

    static {
        crdContext = new CustomResourceDefinitionContext.Builder()
                .withGroup("machinelearning.seldon.io")
                .withPlural("seldondeployments")
                .withScope("Namespaced")
                .withVersion("v1")
                .build();
    }

    private K8sClientUtils(K8sProperties k8sProperties){
        init(k8sProperties);
    }

    private void init(K8sProperties k8sProperties) {
        this.k8sProperties = k8sProperties;
    }

    public static K8sClientUtils getInstance(K8sProperties k8sProperties){
        if(k8sClientUtils == null){
            synchronized (MinioClientUtils.class){
                if(k8sClientUtils == null){
                    k8sClientUtils = new K8sClientUtils(k8sProperties);
                    return k8sClientUtils;
                }
            }
        }
        return k8sClientUtils;
    }



    private KubernetesClient getConnection(){

        KubernetesClient client = threadLocal.get();

        if(client == null){
            try {

//                String adminConfData = IOUtils.toString(this.k8sProperties.getAdminConf().getInputStream());
//                Config config = Config.fromKubeconfig(adminConfData);

                Config config = new ConfigBuilder().withMasterUrl(k8sProperties.getUrl())
                        .withTrustCerts(true)
                        .withCaCertData(IOUtils.toString(k8sProperties.getCaCrt().getInputStream()))
                        .withClientCertData(Base64.getEncoder().encodeToString(IOUtils.toByteArray(k8sProperties.getClientCrt().getInputStream())))
                        .withClientKeyData(IOUtils.toString(k8sProperties.getClientKey().getInputStream()))
                        // 需将 Namespace 初始化为 null
                        .withNamespace(null)
                        .build();

                client = new DefaultKubernetesClient(config);

//                String is = IOUtils.toString(new FileInputStream(new File("E:\\项目\\github\\model-manager\\src\\main\\resources\\k8s\\admin.conf")));
//                Config config = Config.fromKubeconfig(is);
//                client = new DefaultKubernetesClient(config);


                threadLocal.set(client);
            }catch (Exception e){
                logger.info("连接k8s失败",e);
                K8sClientExceptionEnum.K8s_Connnection_Break.throwException();
            }
        }

        return client;
    }

    /**
     * 创建作业
     * @param job
     * @param watcher
     */
//    public Watch createOrReplaceJob(String namespace,Job job, Watcher watcher){
//
//        try{
//            KubernetesClient client = getConnection();
//            Job resultJob = client.batch().jobs().inNamespace(namespace).createOrReplace(job);
//            return client.pods()
//                    .inNamespace(namespace)
////                    .withLabel("job-name")
//                    .watch(watcher);
//        } catch (Exception e){
//            logger.info("创建作业失败",e);
//            throw new RuntimeException("创建作业失败");
//        }
//
//    }

    /**
     * 创建作业
     * @param job
     * @param watcher
     */
    public Watch createOrReplaceJob(String namespace, Job job, Watcher watcher){

        try{
            KubernetesClient client = getConnection();
            Job resultJob = client.batch().jobs().inNamespace(namespace).createOrReplace(job);

            return client.batch().jobs()
                    .inNamespace(resultJob.getMetadata().getNamespace())
                    .withName(resultJob.getMetadata().getName())
                    .watch(watcher);
        } catch (Exception e){
            logger.info("创建作业失败",e);
            throw new RuntimeException("创建作业失败");
        }

    }

    /**
     * 删除作业
     * @param namespace
     * @param job
     * @param watcher
     * @return
     */
    public Watch deleteJob(String namespace,Job job, Watcher watcher){
        try{
            KubernetesClient client = getConnection();
            Boolean bool = client.batch().jobs().inNamespace(namespace).delete(job);
            return client.pods()
                    .inNamespace(namespace)
//                    .withLabel("job-name")
                    .watch(watcher);
        } catch (Exception e){
            logger.info("创建作业失败",e);
            throw new RuntimeException("创建作业失败");
        }

    }

    /**
     * 删除作业
     * @param namespace
     * @param job
     * @return
     */
    public Boolean deleteJob(String namespace,Job job){
        try{
            KubernetesClient client = getConnection();
            Boolean bool = client.batch().jobs().inNamespace(namespace).delete(job);
            return bool;
        } catch (Exception e){
            logger.info("创建作业失败",e);
            throw new RuntimeException("创建作业失败");
        }
    }


    /**
     * 创建自定义资源
     * @param objectAsString
     * @param watcher
     */
    public Watch createOrReplaceCRD(String namespace,String name, String objectAsString, Watcher watcher){

        try{
            KubernetesClient client = getConnection();
            Map<String, Object> resultMap = client.customResource(crdContext).createOrReplace(namespace,objectAsString);
//            logger.info("Watching custom resources now. CRD json is : {}", Serialization.asJson(resultMap));
//            SeldonDeployment seldonDeployment = Serialization.unmarshal(Serialization.asYaml(resultMap), SeldonDeployment.class);
//            String newNamespace = ((Map<String,String>) resultMap.get("metadata")).get("namespace");
//            String newName = ((Map<String,String>) resultMap.get("metadata")).get("name");
//            String resourceVersion = ((Map<String,String>) resultMap.get("metadata")).get("resourceVersion");
            logger.info("部署SeldonDeployment后返回的json  {} ", Serialization.asJson(resultMap));
            return client.customResource(crdContext).watch(namespace,null , null,  null, watcher );
        } catch (Exception e){
            logger.info("创建SeldonDeployment自定义资源失败",e);
            throw new RuntimeException("创建SeldonDeployment自定义资源失败");
        }

    }


    /**
     * 删除自定义资源
     * @param namespace
     * @param name
     */
    public Watch deleteCRD(String namespace, String name, Watcher watcher){

        try{
            KubernetesClient client = getConnection();
            logger.info("Watching custom resources now");
            Watch watch = client.customResource(crdContext).watch(namespace,null,null,null,watcher);
            Map<String, Object> deleted  = client.customResource(crdContext).delete(namespace, name);
            return watch;
        } catch (Exception e){
            logger.info("删除SeldonDeployment自定义资源失败",e);
            throw new RuntimeException("删除SeldonDeployment自定义资源失败");
        }

    }

    /**
     * 部署deployment资源
     * @param namespace
     * @param name
     * @param deployment
     * @param watcher
     * @return
     */
    public Watch createOrReplaceDeployment(String namespace, String name, Deployment deployment, Watcher watcher){
        try{
            KubernetesClient client = getConnection();
            client.apps().deployments().inNamespace(namespace).createOrReplace(deployment);
            Watch watch = client.apps().deployments().inNamespace(namespace).withName(name).watch(watcher);
            return watch;
        } catch (Exception e){
            logger.info("部署Deployment资源失败",e);
            throw new RuntimeException("部署Deployment资源失败");
        }
    }

    public Watch deleteDeployment(String namespace, String name, Deployment deployment, Watcher watcher){
        try{
            KubernetesClient client = getConnection();
            client.apps().deployments().inNamespace(namespace).delete(deployment);
            Watch watch = client.apps().deployments().inNamespace(namespace).withName(name).watch(watcher);
            return watch;
        } catch (Exception e){
            logger.info("部署Deployment资源失败",e);
            throw new RuntimeException("部署Deployment资源失败");
        }
    }

    public void deleteDeployment(String namespace, String name, Deployment deployment){
        try{
            KubernetesClient client = getConnection();
            client.apps().deployments().inNamespace(namespace).delete(deployment);
        } catch (Exception e){
            logger.info("部署Deployment资源失败",e);
            throw new RuntimeException("部署Deployment资源失败");
        }
    }

    /**
     * 部署Service资源
     * @param namespace
     * @param name
     * @param service
     * @param watcher
     * @return
     */
    public Watch createOrReplaceService(String namespace, String name, Service service, Watcher watcher){
        try{
            KubernetesClient client = getConnection();
            client.services().inNamespace(namespace).createOrReplace(service);
            Watch watch = client.services().inNamespace(namespace).withName(name).watch(watcher);
            return watch;
        } catch (Exception e){
            logger.info("部署Service资源失败",e);
            throw new RuntimeException("部署Service资源失败");
        }
    }

    /**
     * 部署Service资源
     * @param namespace
     * @param name
     * @param service
     * @return
     */
    public void createOrReplaceService(String namespace, String name, Service service){
        try{
            KubernetesClient client = getConnection();
            client.services().inNamespace(namespace).createOrReplace(service);
        } catch (Exception e){
            logger.info("部署Service资源失败",e);
            throw new RuntimeException("部署Service资源失败");
        }
    }

    public Watch deleteService(String namespace, String name, Service service, Watcher watcher){
        try{
            KubernetesClient client = getConnection();
            client.services().inNamespace(namespace).delete(service);
            Watch watch = client.services().inNamespace(namespace).withName(name).watch(watcher);
            return watch;
        } catch (Exception e){
            logger.info("删除Service资源失败",e);
            throw new RuntimeException("删除Service资源失败");
        }
    }

    public void deleteService(String namespace, String name, Service service){
        try{
            KubernetesClient client = getConnection();
            client.services().inNamespace(namespace).delete(service);
        } catch (Exception e){
            logger.info("删除Service资源失败",e);
            throw new RuntimeException("删除Service资源失败");
        }
    }


    /**
     * 查看pod日志
     * @param namespace
     * @param name
     * @param container
     */
    public String watchPodLog(String namespace, String name, String container){
        try{
            KubernetesClient client = getConnection();
            logger.info("watch server log");
            return client.pods().inNamespace(namespace).withName(name).inContainer(container).getLog();
        } catch (Exception e){
            logger.info("查看pod日志失败",e);
            throw new RuntimeException("查看pod日志失败");
        }

    }

    /**
     * 获取Deployment
     * @param namespace
     * @param name
     * @return
     */
    public Deployment getDeployment(String namespace, String name) {
        try{
            KubernetesClient client = getConnection();
            return client.apps().deployments().inNamespace(namespace).withName(name).get();
        } catch (Exception e){
            logger.info("获取Deployment失败: {} : {} ",namespace,name,e);
            throw new RuntimeException("获取Deployment失败");
        }
    }

    /**
     * 获取Pod
     * @param deployment
     * @return
     */
    public Pod getPod(Deployment deployment) {
        try{
            KubernetesClient client = getConnection();
            PodList podList = client.pods().inNamespace(deployment.getMetadata().getNamespace()).withLabelSelector(deployment.getSpec().getSelector()).list();
            if(podList == null || podList.getItems() == null || podList.getItems().size() < 1){
                throw new RuntimeException("没有获取到Pod信息");
            }
            List<Pod> pods = podList.getItems();
            return pods.get(0);
        } catch (Exception e){
            logger.info("获取Pod失败: {} : {} ",deployment.getMetadata().getNamespace(), e);
            throw new RuntimeException("获取Pod失败");
        }

    }

    /**
     * 获取可用CPU、内存总量
     * @return
     */
    public Map<String,String> getTotalCPUMemory() {
        try{
            KubernetesClient client = getConnection();
            NodeMetricsList nodeMetricList = client.top().nodes().metrics();
//            return nodeMetricList.getItems().stream().map(metric -> {
//                metric.getUsage();
//            });
            return null;
        } catch (Exception e){
            logger.info("获取CPU、内存总量失败", e);
            throw new RuntimeException("获取CPU、内存总量失败");
        }

    }

    /**
     * 设置运行容器的cpu和内存
     * @param namespace
     * @param podName
     * @param container
     */
    public void setContainerMemory(String namespace, String podName, Container container) {
        try{
            KubernetesClient client = getConnection();
            PodMetrics podMetrics = client.top().pods().metrics(namespace,podName);
            Optional<ContainerMetrics> containerMetrics = podMetrics.getContainers().stream().filter(cm -> cm.getName().equals(container.getNameEn())).findFirst();
            containerMetrics.ifPresent(c -> {
                Map<String, Quantity> usage =  c.getUsage();
                container.setCpu(usage.get("cpu").getAmount());
                container.setMemory(usage.get("memory").getAmount());
            });
        } catch (Exception e){
            logger.info("获取CPU、内存总量失败", e);
            throw new RuntimeException("获取CPU、内存总量失败");
        }

    }

    public Map<String,Double> getFreeCpuAndMemory() {
        try{
            KubernetesClient client = getConnection();
            // 查询总量
            NodeList nodeList = client.nodes().withLabelIn("node-role.kubernetes.io/master").list();
            // 查询限额
            PodList podList = client.pods().inAnyNamespace().list();
            List<Pod> pods = podList.getItems().stream().filter(pod -> !"master".equals(pod.getSpec().getNodeName())).collect(Collectors.toList());
            // 计算
            final List<Integer> cpuList =  nodeList.getItems().stream().map(node -> {
                Map<String, Quantity> capacity = node.getStatus().getCapacity();
                return capacity == null ? 0 : capacity.get("cpu") == null ? 0 : Integer.parseInt(capacity.get("cpu").getAmount());
            }).collect(Collectors.toList());

            final List<Integer> memoryList =  nodeList.getItems().stream().map(node -> {
                Map<String, Quantity> capacity = node.getStatus().getCapacity();
                return capacity == null ? 0 : capacity.get("memory") == null ? 0 : Integer.parseInt(capacity.get("memory").getAmount());
            }).collect(Collectors.toList());

            final List<io.fabric8.kubernetes.api.model.Container> containers = pods.stream().flatMap(pod -> pod.getSpec().getContainers().stream()).collect(Collectors.toList());
            final List<Integer> cpuLimitList = containers.stream().map(container -> {
                ResourceRequirements resourceRequirements = container.getResources();
                Map<String, Quantity> limits = resourceRequirements.getLimits();
                return limits == null ? 0 : limits.get("cpu") == null ? 0 : Integer.parseInt(limits.get("cpu").getAmount());
            }).collect(Collectors.toList());

            final List<Integer> memoryLimitList = containers.stream().map(container -> {
                ResourceRequirements resourceRequirements = container.getResources();
                Map<String, Quantity> limits = resourceRequirements.getLimits();
                return limits == null ? 0 : limits.get("memory") == null ? 0 : Integer.parseInt(limits.get("memory").getAmount());
            }).collect(Collectors.toList());

            final List<Integer> cpuRequestList = containers.stream().map(container -> {
                ResourceRequirements resourceRequirements = container.getResources();
                Map<String, Quantity> requests = resourceRequirements.getRequests();
                return requests == null ? 0 : requests.get("cpu") == null ? 0 : Integer.parseInt(requests.get("cpu").getAmount());
            }).collect(Collectors.toList());

            final List<Integer> memoryRequestList = containers.stream().map(container -> {
                ResourceRequirements resourceRequirements = container.getResources();
                Map<String, Quantity> requests = resourceRequirements.getRequests();
                return requests == null ? 0 : requests.get("memory") == null ? 0 : Integer.parseInt(requests.get("memory").getAmount());
            }).collect(Collectors.toList());

//            logger.info();

            Integer totalCPU = cpuList.stream().reduce(0,(sum,item) -> sum + item);
            Integer totalMemory = memoryList.stream().reduce(0,(sum,item) -> sum + item);
            Integer totalLimitCPU = cpuLimitList.stream().reduce(0,(sum,item) -> sum + item);
            Integer totalRequestCPU = cpuRequestList.stream().reduce(0,(sum,item) -> sum + item);
            Integer totalLimitMemory = memoryLimitList.stream().reduce(0,(sum,item) -> sum + item);
            Integer totalRequestMemory = memoryRequestList.stream().reduce(0,(sum,item) -> sum + item);

            Map<String,Double> result = Maps.newHashMap();
            result.put("freeCPU",totalCPU * 1000 * 0.9 - totalLimitCPU);
            result.put("freeMemory",totalMemory * 0.9 - totalLimitMemory);
            return result;
        } catch (Exception e){
            logger.info("获取可分配CPU、内存失败", e);
            throw new RuntimeException("获取可分配CPU、内存失败");
        }
    }

}
