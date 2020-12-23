package com.zkwg.modelmanager.k8s;

import com.zkwg.modelmanager.core.Handler;
import com.zkwg.modelmanager.core.IMessage;
import com.zkwg.modelmanager.core.K8sDeployResult;
import com.zkwg.modelmanager.core.K8sDeployResultMessage;
import com.zkwg.modelmanager.utils.K8sClientUtils;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentStatus;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class DeploymentWatcher implements Watcher<Deployment>, Supplier<IMessage<K8sDeployResult>> {

    private static Logger logger = LoggerFactory.getLogger(DeploymentWatcher.class);

    private volatile boolean flag = false;

    private Integer tenantId;

    public K8sClientUtils k8sClientUtils;

    private Deployment deployment;

    private Handler<IMessage<K8sDeployResult>> handler;

    private volatile IMessage<K8sDeployResult> message;

    public DeploymentWatcher(Integer tenantId,Deployment deployment, K8sClientUtils k8sClientUtils, Handler<IMessage<K8sDeployResult>> handler) {
        super();
        this.tenantId = tenantId;
        this.k8sClientUtils = k8sClientUtils;
        this.deployment = deployment;
        this.handler = handler;
    }

    @Override
    public void eventReceived(Action action, Deployment deployment) {
        logger.info("{}: {}", action, Serialization.asJson(deployment));
//        IMessage<JobRunResult> message = getMessage(jobPod);
//        handler.handlerMessage(message);
        DeploymentStatus status = deployment.getStatus();
        if( status != null && status.getAvailableReplicas() != null && status.getAvailableReplicas() > 0 ){
            this.flag = true;
            this.message = new K8sDeployResultMessage(new K8sDeployResult(this.tenantId, K8sDeployResult.Status.RUNNING, Serialization.asJson(deployment)));
        }
        if ( action == Action.ERROR ) {
            this.flag = true;
            this.message = new K8sDeployResultMessage(new K8sDeployResult(this.tenantId, K8sDeployResult.Status.ERROR,Serialization.asJson(deployment)));
        }
    }


    @Override
    public void onClose(KubernetesClientException e) {
        logger.debug("Watcher onClose");
        if (e != null) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public IMessage<K8sDeployResult> get() {
        try {
            logger.info("部署Json:  " + deployment.toString());
            ObjectMeta objectMeta = deployment.getMetadata();
            Watch watch =  k8sClientUtils.createOrReplaceDeployment(objectMeta.getNamespace(),objectMeta.getName(), deployment, this);
            while(!flag) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            watch.close();
        } catch (Exception e) {
            this.message = new K8sDeployResultMessage(new K8sDeployResult(this.tenantId, K8sDeployResult.Status.ERROR,""));
        }
        return this.message;
    }


}