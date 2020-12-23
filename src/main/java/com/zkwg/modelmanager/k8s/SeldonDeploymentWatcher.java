package com.zkwg.modelmanager.k8s;

import com.zkwg.modelmanager.core.DeployResult;
import com.zkwg.modelmanager.core.DeployResultMessage;
import com.zkwg.modelmanager.core.IMessage;
import com.zkwg.modelmanager.entity.seldon.SeldonDeployment;
import com.zkwg.modelmanager.entity.seldon.SeldonDeploymentStatus;
import com.zkwg.modelmanager.utils.K8sClientUtils;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SeldonDeploymentWatcher extends BaseWatcher<String,IMessage<DeployResult>> {

    private static Logger logger = LoggerFactory.getLogger(SeldonDeploymentWatcher.class);

    private volatile boolean flag = false;

    private volatile IMessage<DeployResult> message;

    public SeldonDeploymentWatcher(SeldonDeployment seldonDeployment, K8sClientUtils k8sClientUtils) {
        super();
        this.seldonDeployment = seldonDeployment;
        this.k8sClientUtils = k8sClientUtils;
        this.deployInstanceJson = Serialization.asJson(seldonDeployment);
        this.namespace = seldonDeployment.getMetadata().getNamespace();
        this.instanceName = seldonDeployment.getMetadata().getName();
    }

    @Override
    public void eventReceived(Action action, String resource) {
        logger.info("{}: {}", action, resource);
        SeldonDeployment seldonDeployment = Serialization.unmarshal(resource,SeldonDeployment.class);
        SeldonDeploymentStatus status = seldonDeployment.getStatus();
        ObjectMeta meta = seldonDeployment.getMetadata();
        if( status != null && "Available".equals(status.getState()) && instanceName.equals(meta.getName()) ){
            this.flag = true;
            this.message = new DeployResultMessage(new DeployResult(DeployResult.Status.RUNNING,seldonDeployment));
        }
        if ( ( action == Action.ERROR && instanceName.equals(meta.getName() ) ) ||
             ( status != null && "Failed".equals(status.getState()) && instanceName.equals(meta.getName()) )
           ) {
            this.flag = true;
            this.message = new DeployResultMessage(new DeployResult(DeployResult.Status.ERROR,seldonDeployment));
        }
    }


    @Override
    public IMessage<DeployResult> call() throws Exception {
        Watch watch =  k8sClientUtils.createOrReplaceCRD(namespace,instanceName,deployInstanceJson, this);
        while(!flag) {
            Thread.sleep(500);
        }
        watch.close();
        return this.message;
    }
}