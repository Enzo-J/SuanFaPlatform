package com.zkwg.modelmanager.core;

import com.zkwg.modelmanager.core.model.FlaskModelProcess;
import com.zkwg.modelmanager.core.model.ModelProcess;
import com.zkwg.modelmanager.core.model.SeldonModelProcess;
import com.zkwg.modelmanager.core.server.FlaskServerProcess;
import com.zkwg.modelmanager.core.server.SeldonServerProcess;
import com.zkwg.modelmanager.core.server.ServerProcess;
import com.zkwg.modelmanager.k8s.K8sOperator;
import com.zkwg.modelmanager.service.IContainerService;
import com.zkwg.modelmanager.service.IDeployProcessDefService;
import com.zkwg.modelmanager.service.IModelService;
import com.zkwg.modelmanager.service.IServerService;
import com.zkwg.modelmanager.utils.DockerProperties;
import com.zkwg.modelmanager.utils.MinioProperties;
import com.zkwg.modelmanager.ws.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ProcessFactory {

    private static ApplicationContext context;
//    private static ConfigurableApplicationContext context = SpringApplication.run(ModelManagerApplication.class, new String[]{});

    /**
     * 生成通用模型处理类
     * @return
     */
    public static ModelProcess getSeldonlModelProcess(){
        MinioProperties minioProperties = context.getBean(MinioProperties.class);
        IModelService modelService = context.getBean(IModelService.class);
        IDeployProcessDefService deployProcessDefService = context.getBean(IDeployProcessDefService.class);
        return new SeldonModelProcess(minioProperties,modelService,deployProcessDefService);
    }

    /**
     * 生成Flask模型处理类
     * @return
     */
    public static ModelProcess getFlaskModelProcess() {
        DockerProperties dockerProperties = context.getBean(DockerProperties.class);
        MinioProperties minioProperties = context.getBean(MinioProperties.class);
        IModelService modelService = context.getBean(IModelService.class);
        IDeployProcessDefService deployProcessDefService = context.getBean(IDeployProcessDefService.class);
        return new FlaskModelProcess(dockerProperties,minioProperties,modelService,deployProcessDefService);
    }

    /**
     * 生成Seldon服务处理类
     * @return
     */
    public static ServerProcess getSeldonlServerProcess(){

        IServerService serverService = context.getBean(IServerService.class);

        IModelService modelService = context.getBean(IModelService.class);

        IContainerService containerService = context.getBean(IContainerService.class);

        K8sOperator k8sOperator = context.getBean(K8sOperator.class);

        return new SeldonServerProcess(containerService, modelService, serverService, k8sOperator);
    }

    /**
     * 生成Flask服务处理类
     * @return
     */
    public static ServerProcess getFlaskServerProcess(){

        IServerService serverService = context.getBean(IServerService.class);

        IModelService modelService = context.getBean(IModelService.class);

        IContainerService containerService = context.getBean(IContainerService.class);

        WebSocketServer webSocketServer = context.getBean(WebSocketServer.class);

        K8sOperator k8sOperator = context.getBean(K8sOperator.class);

        return new FlaskServerProcess(containerService, modelService, serverService, webSocketServer, k8sOperator);
    }

    @Autowired
    public void setContext(ApplicationContext context) {
        ProcessFactory.context = context;
    }
}
