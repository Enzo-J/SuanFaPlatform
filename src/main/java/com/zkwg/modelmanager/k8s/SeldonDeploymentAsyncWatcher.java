package com.zkwg.modelmanager.k8s;

import com.zkwg.modelmanager.core.DeployResult;
import com.zkwg.modelmanager.core.DeployResultMessage;
import com.zkwg.modelmanager.core.IMessage;
import com.zkwg.modelmanager.entity.Server;
import com.zkwg.modelmanager.entity.seldon.SeldonDeployment;
import com.zkwg.modelmanager.entity.seldon.SeldonDeploymentStatus;
import com.zkwg.modelmanager.utils.K8sClientUtils;
import com.zkwg.modelmanager.ws.WebSocketServer;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class SeldonDeploymentAsyncWatcher implements Watcher<String>, Supplier<IMessage<DeployResult>> {

    private static Logger logger = LoggerFactory.getLogger(SeldonDeploymentAsyncWatcher.class);

    public String namespace;

    public String instanceName;

    public String deployInstanceJson;

    public SeldonDeployment seldonDeployment;

    public K8sClientUtils k8sClientUtils;

    private WebSocketServer webSocketServer;

    private String userId;

    private Server server;

    private volatile boolean flag = false;

    private Integer tenantId;

    private volatile IMessage<DeployResult> message;

    public SeldonDeploymentAsyncWatcher(Integer tenantId,String userId, Server server, K8sClientUtils k8sClientUtils, WebSocketServer webSocketServer) {
        this.tenantId = tenantId;
        this.seldonDeployment = Serialization.unmarshal(server.getServerJson(), SeldonDeployment.class);;
        this.k8sClientUtils = k8sClientUtils;
        this.deployInstanceJson = server.getServerJson();
        this.namespace = seldonDeployment.getMetadata().getNamespace();
        this.instanceName = seldonDeployment.getMetadata().getName();
        this.webSocketServer = webSocketServer;
        this.userId = userId;
        this.server = server;
    }

    @Override
    public void eventReceived(Action action, String resource) {
        logger.info("{}: {}", action, resource);
        SeldonDeployment seldonDeployment = Serialization.unmarshal(resource,SeldonDeployment.class);
        SeldonDeploymentStatus status = seldonDeployment.getStatus();
        ObjectMeta meta = seldonDeployment.getMetadata();
        // 只查看自己部署的SeldonDeployment
        if(instanceName.equals(meta.getName())) {
            if( status != null && "Available".equals(status.getState())){
                this.flag = true;
                this.message = new DeployResultMessage(new DeployResult(this.tenantId, DeployResult.Status.RUNNING,seldonDeployment,server,webSocketServer,userId));
            }
            if("Failed".equals(status.getState()) && status.getDescription().contains("please apply your changes to the latest version and try again")) {
                logger.info("跳过resourceVersion问题，k8s会重新部署");
                return;
            }
            if ( action == Action.ERROR || status != null && "Failed".equals(status.getState()) ) {
                this.flag = true;
                logger.error("seldonDeployment 部署出错 ： " + status.getDescription());
                this.message = new DeployResultMessage(new DeployResult(this.tenantId, DeployResult.Status.ERROR,seldonDeployment,server,webSocketServer,userId));
            }
        }

    }

    @Override
    public void onClose(KubernetesClientException e) {
        logger.debug("Watcher onClose");
        if (e != null) {
            logger.error(e.getMessage(), e);
        }
    }


//    @Override
//    public IMessage<DeployResult> call() throws Exception {
//        Watch watch =  k8sClientUtils.createOrReplaceCRD(namespace,instanceName,deployInstanceJson, this);
//        while(!flag) {
//            Thread.sleep(500);
//        }
//        watch.close();
//        return this.message;
//    }

    @Override
    public IMessage<DeployResult> get() {
       try {
           logger.info("部署Json:  " + deployInstanceJson);
           Watch watch =  k8sClientUtils.createOrReplaceCRD(namespace,instanceName,deployInstanceJson, this);
           while(!flag) {
               try {
                   Thread.sleep(500);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
           watch.close();
       } catch (Exception e) {
           this.message = new DeployResultMessage(new DeployResult(this.tenantId, DeployResult.Status.ERROR,seldonDeployment,server,webSocketServer,userId));
       }
       return this.message;
    }
}