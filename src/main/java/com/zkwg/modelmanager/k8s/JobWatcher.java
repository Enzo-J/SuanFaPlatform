package com.zkwg.modelmanager.k8s;

import com.zkwg.modelmanager.core.*;
import com.zkwg.modelmanager.entity.Server;
import com.zkwg.modelmanager.entity.seldon.SeldonDeployment;
import com.zkwg.modelmanager.entity.seldon.SeldonDeploymentStatus;
import com.zkwg.modelmanager.utils.K8sClientUtils;
import com.zkwg.modelmanager.utils.MD5Util;
import com.zkwg.modelmanager.ws.WebSocketServer;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.PodStatus;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.api.model.batch.JobStatus;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.function.Supplier;

public class JobWatcher implements Watcher<Job>, Runnable {

    private static Logger logger = LoggerFactory.getLogger(JobWatcher.class);

    private volatile boolean flag = false;

    public K8sClientUtils k8sClientUtils;

    private String jobJsonMd5;

    private Job  job;

    private Handler<IMessage> handler;

    private Integer tenantId;

    private volatile IMessage<JobRunResult> message;

    public JobWatcher(Job job, K8sClientUtils k8sClientUtils, Handler<IMessage> handler, Integer tenantId) {
        super();
        this.k8sClientUtils = k8sClientUtils;
        this.job = job;
        this.tenantId = tenantId;
        this.handler = handler;
        this.jobJsonMd5 = MD5Util.getMD5(Serialization.asJson(this.job));
    }

    @Override
    public void eventReceived(Action action, Job jobPod) {
        logger.info("{}: {}", action, Serialization.asJson(jobPod));
        IMessage<JobRunResult> message = getMessage(jobPod);
        handler.handlerMessage(message);
    }

    private IMessage<JobRunResult> getMessage(Job jobPod) {

        JobStatus jobStatus = jobPod.getStatus();
        JobRunResult jobRunResult = new JobRunResult();
        jobRunResult.setJobJsonMd5(this.jobJsonMd5);
        jobRunResult.setRunningPodJson(Serialization.asJson(jobPod));

        if(jobStatus == null) {
            jobRunResult.setStatus(JobRunResult.Status.PENDING);
        }

        if(jobStatus.getActive() != null && jobStatus.getActive() > 0) {
            jobRunResult.setStatus(JobRunResult.Status.RUNNING);
        }

        if(jobStatus.getSucceeded() != null && jobStatus.getSucceeded() > 0){
            flag = true;
            jobRunResult.setStatus(JobRunResult.Status.SUCCESS);
        }
        BaseContextHandler.setTenant(this.tenantId);
        return new JobRunResultMessage(jobRunResult);
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
        logger.info("部署Json:  " + job);
        Watch watch =  k8sClientUtils.createOrReplaceJob(job.getMetadata().getNamespace(), job, this);
        while(!flag) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        watch.close();
    }


}