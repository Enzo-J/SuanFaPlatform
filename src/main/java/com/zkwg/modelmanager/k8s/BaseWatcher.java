package com.zkwg.modelmanager.k8s;

import com.zkwg.modelmanager.entity.seldon.SeldonDeployment;
import com.zkwg.modelmanager.utils.K8sClientUtils;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public abstract class BaseWatcher<W,C> implements Watcher<W>, Callable<C> {

    private static Logger logger = LoggerFactory.getLogger(SeldonDeploymentWatcher.class);

    public String namespace;

    public String instanceName;

    public String deployInstanceJson;

    public SeldonDeployment seldonDeployment;

    public K8sClientUtils k8sClientUtils;

    public BaseWatcher(){}

    public BaseWatcher(SeldonDeployment seldonDeployment, K8sClientUtils k8sClientUtils) {
        this.seldonDeployment = seldonDeployment;
        this.k8sClientUtils = k8sClientUtils;
        this.deployInstanceJson = Serialization.asJson(seldonDeployment);
        this.namespace = seldonDeployment.getMetadata().getNamespace();
        this.instanceName = seldonDeployment.getMetadata().getName();
    }


    @Override
    public void onClose(KubernetesClientException e) {
        logger.debug("Watcher onClose");
        if (e != null) {
            logger.error(e.getMessage(), e);
        }
    }
}
