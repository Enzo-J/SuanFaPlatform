package com.zkwg.modelmanager.k8s;

import com.zkwg.modelmanager.ModelManagerApplication;
import com.zkwg.modelmanager.entity.Server;
import com.zkwg.modelmanager.service.IServerService;
import com.zkwg.modelmanager.ws.WebSocketServer;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelManagerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class K8sOperatorTest {

    private static Logger logger = LoggerFactory.getLogger(K8sOperatorTest.class);

    @Autowired
    private K8sOperator k8sOperator;

    @Autowired
    private IServerService serverService;

    @Autowired
    private WebSocketServer webSocketServer;

    @Test
    void deployDeployment() throws InterruptedException {

        Server server = serverService.selectByPrimaryKey(66);

        String serverJson = server.getServerJson();

        new DeployServerThread(1,"1", server,k8sOperator, serverService, webSocketServer).run();

        while (true) {
            Thread.sleep(500);
        }
//        logger.info("Done !!!");
    }

}