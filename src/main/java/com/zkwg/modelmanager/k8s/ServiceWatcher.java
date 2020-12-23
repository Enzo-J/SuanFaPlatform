package com.zkwg.modelmanager.k8s;

import com.zkwg.modelmanager.core.*;
import com.zkwg.modelmanager.utils.K8sClientUtils;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodStatus;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class ServiceWatcher implements Watcher<Service>, Supplier<IMessage<K8sDeployResult>> {

    private static Logger logger = LoggerFactory.getLogger(ServiceWatcher.class);

    private volatile boolean flag = false;

    public K8sClientUtils k8sClientUtils;

    private String jobJsonMd5;

    private Service service;

    private Integer tenantId;

    private Handler<IMessage<K8sDeployResult>> handler;

    private volatile IMessage<K8sDeployResult> message;

    public ServiceWatcher(Integer tenantId,Service service, K8sClientUtils k8sClientUtils, Handler<IMessage<K8sDeployResult>> handler) {
        super();
        this.tenantId = tenantId;
        this.k8sClientUtils = k8sClientUtils;
        this.service = service;
        this.handler = handler;
    }

    @Override
    public void eventReceived(Action action, Service service) {
        logger.info("{}: {}", action, Serialization.asJson(service));
//        IMessage<JobRunResult> message = getMessage(jobPod);
//        handler.handlerMessage(message);
        if( action == Action.ADDED ) {
            this.flag = true;
            this.message = new K8sDeployResultMessage(new K8sDeployResult(this.tenantId,K8sDeployResult.Status.RUNNING, Serialization.asJson(service)));
        }
        if ( action == Action.ERROR ) {
            this.flag = true;
            this.message = new K8sDeployResultMessage(new K8sDeployResult(this.tenantId, K8sDeployResult.Status.ERROR,Serialization.asJson(service)));
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
            logger.info("部署Json:  " + service.toString());
            ObjectMeta objectMeta = service.getMetadata();
            Watch watch =  k8sClientUtils.createOrReplaceService(objectMeta.getNamespace(),objectMeta.getName(), service, this);
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