//package com.zkwg.modelmanager.k8s;
//
//import com.zkwg.modelmanager.core.DeployResult;
//import com.zkwg.modelmanager.core.DeployResultMessage;
//import com.zkwg.modelmanager.core.IMessage;
//import com.zkwg.modelmanager.entity.Server;
//import com.zkwg.modelmanager.entity.seldon.SeldonDeployment;
//import com.zkwg.modelmanager.entity.seldon.SeldonDeploymentStatus;
//import com.zkwg.modelmanager.utils.K8sClientUtils;
//import com.zkwg.modelmanager.ws.WebSocketServer;
//import io.fabric8.kubernetes.api.model.ObjectMeta;
//import io.fabric8.kubernetes.client.Watch;
//import io.fabric8.kubernetes.client.utils.Serialization;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class SeldonDeploymentWithWSWatcher extends BaseWatcher<String,IMessage<DeployResult>> {
//
//    private static Logger logger = LoggerFactory.getLogger(SeldonDeploymentWithWSWatcher.class);
//
//    private WebSocketServer webSocketServer;
//
//    private String userId;
//
//    private Server server;
//
//    private volatile boolean flag = false;
//
//    private volatile IMessage<DeployResult> message;
//
//    public SeldonDeploymentWithWSWatcher(String userId, Server server, K8sClientUtils k8sClientUtils,WebSocketServer webSocketServer) {
//        super();
//        this.server = server;
//        this.seldonDeployment = Serialization.unmarshal(server.getServerJson(), SeldonDeployment.class);;
//        this.k8sClientUtils = k8sClientUtils;
//        this.deployInstanceJson = server.getServerJson();
//        this.namespace = seldonDeployment.getMetadata().getNamespace();
//        this.instanceName = seldonDeployment.getMetadata().getName();
//        this.webSocketServer = webSocketServer;
//        this.userId = userId;
//    }
//
//    @Override
//    public void eventReceived(Action action, String resource) {
//        logger.info("{}: {}", action, resource);
//        SeldonDeployment seldonDeployment = Serialization.unmarshal(resource,SeldonDeployment.class);
//        SeldonDeploymentStatus status = seldonDeployment.getStatus();
//        ObjectMeta meta = seldonDeployment.getMetadata();
//        try {
//            webSocketServer.sendInfo(Serialization.asJson(status),userId);
//        } catch (Exception e) {
//            logger.error("推送部署消息异常！！！！ ",e);
//        }
//        if( status != null && "Available".equals(status.getState()) && instanceName.equals(meta.getName()) ){
//            this.flag = true;
//            this.message = new DeployResultMessage(new DeployResult(DeployResult.Status.RUNNING,seldonDeployment,server,webSocketServer,userId));
//        }
//        if ( ( action == Action.ERROR && instanceName.equals(meta.getName() ) ) ||
//             ( status != null && "Failed".equals(status.getState()) && instanceName.equals(meta.getName()) )
//           ) {
//            this.flag = true;
//            this.message = new DeployResultMessage(new DeployResult(DeployResult.Status.ERROR,seldonDeployment,server,webSocketServer,userId));
//        }
//
//    }
//
//
//    @Override
//    public IMessage<DeployResult> call() throws Exception {
//        Watch watch =  k8sClientUtils.createOrReplaceCRD(namespace,instanceName,deployInstanceJson, this);
//        while(!flag) {
//            Thread.sleep(500);
//        }
//        watch.close();
//        return this.message;
//    }
//}