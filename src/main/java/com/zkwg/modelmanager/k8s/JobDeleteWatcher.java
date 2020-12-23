package com.zkwg.modelmanager.k8s;

import com.zkwg.modelmanager.core.Handler;
import com.zkwg.modelmanager.core.IMessage;
import com.zkwg.modelmanager.core.JobRunResult;
import com.zkwg.modelmanager.utils.K8sClientUtils;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodStatus;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class JobDeleteWatcher implements Watcher<Pod>, Runnable {

    private static Logger logger = LoggerFactory.getLogger(JobDeleteWatcher.class);

    private volatile boolean flag = false;

    public K8sClientUtils k8sClientUtils;

    private Job  job;

    private Handler<IMessage> handler;

    private volatile IMessage<JobRunResult> message;

    public JobDeleteWatcher(Job job, K8sClientUtils k8sClientUtils, Handler<IMessage> handler) {
        super();
        this.k8sClientUtils = k8sClientUtils;
        this.job = job;
        this.handler = handler;
    }

    @Override
    public void eventReceived(Action action, Pod jobPod) {
        logger.info("{}: {}", action, Serialization.asJson(jobPod));
        PodStatus jobStatus = jobPod.getStatus();
        IMessage<T> message = getMessageFromStatus(action,jobStatus);
        handler.handlerMessage(message);
        updateFlag(message);
    }

    private void updateFlag(IMessage<T> message) {

    }

    private IMessage<T> getMessageFromStatus(Action action,PodStatus jobStatus) {

        return null;
    }

    @Override
    public void onClose(KubernetesClientException e) {
        logger.debug("Watcher onClose");
        if (e != null) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void run() {
        try {
            logger.info("部署Json:  " + job);
            Watch watch =  k8sClientUtils.deleteJob(job.getMetadata().getNamespace(),job, this);
            while(!flag) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            watch.close();
        } catch (Exception e) {
//            this.message = new DeployResultMessage(new DeployResult(DeployResult.Status.ERROR,seldonDeployment,server,webSocketServer,userId));
        }
    }
}