package com.zkwg.modelmanager.k8s;

import com.zkwg.modelmanager.entity.seldon.SeldonDeployment;
import com.zkwg.modelmanager.entity.seldon.SeldonDeploymentStatus;
import com.zkwg.modelmanager.service.impl.ServerServiceImpl;
import com.zkwg.modelmanager.utils.K8sClientUtils;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class CreateCRDWatcher implements Watcher<String>, Callable<SeldonDeployment> {


    private static Logger logger = LoggerFactory.getLogger(CreateCRDWatcher.class);

    private final String namespace;

    private final String instanceName;

    private final String deployInstanceJson;

    private final SeldonDeployment seldonDeployment;

    private final K8sClientUtils k8sClientUtils;

    private SeldonDeployment seldonDeploymentResult;

    private volatile boolean flag = false;

    public CreateCRDWatcher(SeldonDeployment seldonDeployment,K8sClientUtils k8sClientUtils) {
        this.seldonDeployment = seldonDeployment;
        this.k8sClientUtils = k8sClientUtils;
        this.deployInstanceJson = Serialization.asJson(seldonDeployment);
        this.namespace = seldonDeployment.getMetadata().getNamespace();
        this.instanceName = seldonDeployment.getMetadata().getName();
    }

    @Override
    public void eventReceived(Watcher.Action action, String reource) {
        logger.info("{}: {}", action, reource);
        SeldonDeployment seldonDeployment = Serialization.unmarshal(reource,SeldonDeployment.class);
        SeldonDeploymentStatus status = seldonDeployment.getStatus();
        ObjectMeta meta = seldonDeployment.getMetadata();
        if(status != null && ("Available".equals(status.getState()) || "Failed".equals(status.getState())) && instanceName.equals(meta.getName())){
            flag = true;
            seldonDeploymentResult = seldonDeployment;
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
    public SeldonDeployment call() throws Exception {
        Watch watch =  k8sClientUtils.createOrReplaceCRD(namespace,instanceName,deployInstanceJson, this);
        while(!flag) { Thread.sleep(500); }
        watch.close();
        return seldonDeploymentResult;
    }
}