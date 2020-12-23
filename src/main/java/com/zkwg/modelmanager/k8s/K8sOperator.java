package com.zkwg.modelmanager.k8s;

import com.zkwg.modelmanager.core.*;
import com.zkwg.modelmanager.entity.Server;
import com.zkwg.modelmanager.entity.seldon.SeldonDeployment;
import com.zkwg.modelmanager.utils.K8sClientUtils;
import com.zkwg.modelmanager.utils.K8sProperties;
import com.zkwg.modelmanager.ws.WebSocketServer;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class K8sOperator {

    private static Logger logger = LoggerFactory.getLogger(K8sOperator.class);

    @Autowired
    private K8sProperties k8sProperties;

    @Autowired
    private WebSocketServer webSocketServer;

    private ExecutorService executorService = Executors.newFixedThreadPool(20);

    /**
     * 部署服务
     * @return
     */
    public IMessage<DeployResult> deploy(SeldonDeployment seldonDeployment, long timeout, TimeUnit unit){
        SeldonDeploymentWatcher seldonDeploymentWatcher = new SeldonDeploymentWatcher(seldonDeployment, K8sClientUtils.getInstance(k8sProperties));
        return run(seldonDeployment,timeout,unit,seldonDeploymentWatcher);
    }

    /**
     * 停止服务
     * @return
     */
    public IMessage stop(SeldonDeployment seldonDeployment,long timeout, TimeUnit unit){
        DeleteCRDWatcher deleteCRDWatcher = new DeleteCRDWatcher(seldonDeployment, K8sClientUtils.getInstance(k8sProperties));
        return run(seldonDeployment,timeout,unit,deleteCRDWatcher);
    }

    public IMessage run(SeldonDeployment seldonDeployment,long timeout, TimeUnit unit, BaseWatcher baseWatcher){
        Future<IMessage<DeployResult>> future = executorService.submit(baseWatcher);
        try {
            return future.get(timeout,unit);
            // 要根据结果更新  容器、模型、服务 的状态
        } catch (TimeoutException e) {
            // 超时
            logger.error("服务运行超时！！！  ",e);
            future.cancel(true);
            return new DeployResultMessage(new DeployResult(DeployResult.Status.TIME_OUT,seldonDeployment));
        } catch (Exception e) {
            // 异常
            logger.error("服务运行异常！！！  ",e);
            future.cancel(true);
            return new DeployResultMessage(new DeployResult(DeployResult.Status.ERROR,seldonDeployment));
        }
    }

    public void seldonDeploy(String userId,Server server, int timeout, TimeUnit unit, Handler handler) {

        int tenantId = BaseContextHandler.getTenant();
        try {
            SeldonDeployment seldonDeployment = Serialization.unmarshal(server.getServerJson(), SeldonDeployment.class);
            SeldonDeploymentAsyncWatcher seldonDeploymentAsyncWatcher = new SeldonDeploymentAsyncWatcher(tenantId, userId, server, K8sClientUtils.getInstance(k8sProperties), webSocketServer);

            CompletableFuture<IMessage<DeployResult>> future = CompletableFuture.supplyAsync(seldonDeploymentAsyncWatcher,executorService);
            future.acceptEitherAsync(CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(unit.toMillis(timeout));
                } catch (InterruptedException e) {
                    logger.error("超时任务Thread-sleep异常",e);
                }
//                webSocketServer.sendInfo("TimeOut", userId);
                return new DeployResultMessage(new DeployResult(tenantId, DeployResult.Status.TIME_OUT,seldonDeployment,server,webSocketServer,userId));
            }), (IMessage<DeployResult> message) -> {
                BaseContextHandler.setTenant(message.getMessage().getTenantId());
                handler.handlerMessage(message);
            },executorService);
        } catch (Exception e) {
            // 部署异常
            logger.error("SeldonDeployment部署异常", e);
            IMessage<DeployResult> message = new DeployResultMessage(new DeployResult(tenantId, DeployResult.Status.ERROR,Serialization.unmarshal(server.getServerJson(), SeldonDeployment.class),server,webSocketServer,userId));
            handler.handlerMessage(message);
        }

    }

    public  void runJob(Job job, Handler handler) {
        JobWatcher jobWatcher = new JobWatcher(job,K8sClientUtils.getInstance(k8sProperties),handler, BaseContextHandler.getTenant());
        executorService.submit(jobWatcher);
//        CompletableFuture<IMessage<JobRunResult>> future = CompletableFuture.supplyAsync(jobWatcher,executorService);
//        future.whenCompleteAsync((message, throwable) -> {
//            handler.handlerMessage(message);
//        });
    }

    public  void deleteJob(Job job, Handler handler){
        JobDeleteWatcher jobWatcher = new JobDeleteWatcher(job,K8sClientUtils.getInstance(k8sProperties),handler);
        executorService.submit(jobWatcher);
//        CompletableFuture<IMessage<JobRunResult>> future = CompletableFuture.supplyAsync(jobWatcher,executorService);
//        future.whenCompleteAsync((message, throwable) -> {
//            handler.handlerMessage(message);
//        });
    }

    public  void deleteJob(Job job){
        K8sClientUtils.getInstance(k8sProperties).deleteJob(job.getMetadata().getNamespace(),job);
    }

//    public void deploy(String userId, Server server, int i, TimeUnit minutes, ServerServiceImpl serverService) {
//
//        String serverJson = server.getServerJson();
//        String[] strArr = serverJson.split("---");
//        // 解析对应资源
//        Deployment deployment = Serialization.unmarshal(strArr[0], Deployment.class);
//        Service service = Serialization.unmarshal(strArr[1], Service.class);
//        // 部署资源
//        DeploymentWatcher deploymentWatcher = new DeploymentWatcher(deployment,K8sClientUtils.getInstance(k8sProperties));
//        executorService.submit(deploymentWatcher);
//
//        ServiceWatcher serviceWatcher = new ServiceWatcher(service,K8sClientUtils.getInstance(k8sProperties));
//        executorService.submit(serviceWatcher);
//
//    }

    public void deployDeployment(String userId, Deployment deployment, int timeout, TimeUnit unit, Handler<IMessage<K8sDeployResult>> handler) {
        DeploymentWatcher deploymentWatcher = new DeploymentWatcher(BaseContextHandler.getTenant(),deployment,K8sClientUtils.getInstance(k8sProperties), handler);
//        executorService.submit(deploymentWatcher);
        CompletableFuture<IMessage<K8sDeployResult>> future = CompletableFuture.supplyAsync(deploymentWatcher,executorService);
        future.acceptEitherAsync(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(unit.toMillis(timeout));
            } catch (InterruptedException e) {
                logger.error("超时任务Thread-sleep异常",e);
            }
            return new K8sDeployResultMessage(new K8sDeployResult(BaseContextHandler.getTenant(), K8sDeployResult.Status.TIME_OUT,"",null,webSocketServer,userId));
        }), (IMessage<K8sDeployResult> message) -> {
            BaseContextHandler.setTenant(message.getMessage().getTenantId());
            handler.handlerMessage(message);
        },executorService);

    }

    public void deployService(Service service) {
        ObjectMeta objectMeta = service.getMetadata();
        K8sClientUtils.getInstance(k8sProperties).createOrReplaceService(objectMeta.getNamespace(),objectMeta.getName(), service);
    }

    public void deployService(String userId, Service service, int timeout, TimeUnit unit, Handler<IMessage<K8sDeployResult>> handler) {
        ServiceWatcher serviceWatcher = new ServiceWatcher(BaseContextHandler.getTenant(), service,K8sClientUtils.getInstance(k8sProperties), handler);
//        executorService.submit(serviceWatcher);
        CompletableFuture<IMessage<K8sDeployResult>> future = CompletableFuture.supplyAsync(serviceWatcher,executorService);
        future.acceptEitherAsync(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(unit.toMillis(timeout));
            } catch (InterruptedException e) {
                logger.error("超时任务Thread-sleep异常",e);
            }
            return new K8sDeployResultMessage(new K8sDeployResult(BaseContextHandler.getTenant(), K8sDeployResult.Status.TIME_OUT,"",null,webSocketServer,userId));
        }), (IMessage<K8sDeployResult> message) -> {
            handler.handlerMessage(message);
        },executorService);
    }

    public String watchPodLog(String namespace, String name, String container) {
        return K8sClientUtils.getInstance(k8sProperties).watchPodLog(namespace,name,container);
    }

    public void deleteDeployment(Deployment deployment) {
        ObjectMeta objectMeta = deployment.getMetadata();
        K8sClientUtils.getInstance(k8sProperties).deleteDeployment(objectMeta.getNamespace(), objectMeta.getName(), deployment);
    }

    public void deleteService(Service service) {
        ObjectMeta objectMeta = service.getMetadata();
        K8sClientUtils.getInstance(k8sProperties).deleteService(objectMeta.getNamespace(), objectMeta.getName(), service);
    }

    public Pod getPod(Deployment deployment) {
        return K8sClientUtils.getInstance(k8sProperties).getPod(deployment);
    }

    public Deployment getDeployment(String namespace, String name) {
        return K8sClientUtils.getInstance(k8sProperties).getDeployment(namespace, name);
    }
}
