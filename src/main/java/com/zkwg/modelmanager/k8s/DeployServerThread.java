package com.zkwg.modelmanager.k8s;

import com.zkwg.modelmanager.core.*;
import com.zkwg.modelmanager.entity.Server;
import com.zkwg.modelmanager.service.IServerService;
import com.zkwg.modelmanager.ws.WebSocketServer;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DeployServerThread extends Thread implements Handler<IMessage<K8sDeployResult>> {

    private Logger logger = LoggerFactory.getLogger(DeployServerThread.class);

    private AtomicInteger count = new AtomicInteger(0);

    private Integer tenantId;

    private String userId;

    private Service service;

    private Server server;

    private K8sOperator k8sOperator;

    private IServerService serverService;

    private WebSocketServer webSocketServer;

    public DeployServerThread(Integer tenantId,String userId, Server server, K8sOperator k8sOperator, IServerService serverService, WebSocketServer webSocketServer) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.server = server;
        this.k8sOperator = k8sOperator;
        this.serverService = serverService;
        this.webSocketServer = webSocketServer;
    }

    @Override
    public void run() {
        try {
            BaseContextHandler.setTenant(this.tenantId);
            String serverJson = server.getServerJson();
            String[] strArr = serverJson.split("###");
            // 解析对应资源
            Deployment deployment = Serialization.unmarshal(strArr[0], Deployment.class);
            service = Serialization.unmarshal(strArr[1], Service.class);
            // 设置访问网关
//            setNetWay(service);
            // 部署
            k8sOperator.deployService(service);
            k8sOperator.deployDeployment(userId,deployment,2, TimeUnit.MINUTES,this);
        } catch (Exception e) {
            IMessage<K8sDeployResult> message = new K8sDeployResultMessage(new K8sDeployResult(this.tenantId, K8sDeployResult.Status.ERROR,""));
            serverService.deployError(message);
        }
    }

//    private void setNetWay(Service service) {
//        List<ServicePort> list = service.getSpec().getPorts();
//        for(ServicePort servicePort : list) {
//            ObjectMeta objectMeta = service.getMetadata();
//            String namespace = objectMeta.getNamespace();
//            String name = objectMeta.getName();
//            // 设置网关
//            String mappingName = namespace + name + "_rest_mapping";
//            String prefix = "/flask/" + namespace + "/" + name + "/api/v1.0/predictions";
//            String serviceStr = name + "." + namespace + ":" + servicePort.getPort();
//            String rewritePath =
//            String ambassadorValue = "---\napiVersion: ambassador/v1\nkind: Mapping\nname: " + mappingName + "\nprefix: " + prefix + " \nrewrite: "+ rewritePath +"\nservice: " + serviceStr + " \ntimeout_ms: 3000\n";
//
//            service.getMetadata().setAnnotations(ImmutableMap.of("getambassador.io/config",ambassadorValue));
//        }
//    }

    @Override
    public void handlerMessage(IMessage<K8sDeployResult> message) {

        K8sDeployResult k8sDeployResult =  message.getMessage();
        k8sDeployResult.setServer(server);

        switch (k8sDeployResult.getStatus()) {
            case RUNNING:
                webSocketServer.sendInfo("Success", userId);
                serverService.deploySuccess(message);
                break;
            case ERROR:
                webSocketServer.sendInfo("Failed", userId);
                serverService.deployError(message);
                break;
            case TIME_OUT:
                webSocketServer.sendInfo("TimeOut", userId);
                serverService.deployTimeout(message);
                break;
        }

    }


}
