package com.zkwg.modelmanager.core.server;

import com.zkwg.modelmanager.entity.Server;
import com.zkwg.modelmanager.k8s.K8sOperator;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.service.IServerService;
import com.zkwg.modelmanager.service.impl.ServerServiceImpl;

public abstract class ServerProcess {

    public abstract Server create(Server server);

    public abstract void redeploy(String userId, K8sOperator k8sOperator, Server server);

    public abstract R stop(K8sOperator k8sOperator, Server server);

    public abstract String watchLog(Server server);

    public abstract void delete(Server server);

    public abstract String test(Server server);

//    public abstract void redeploy(Server server, String userId) throws Exception;
//
//    public abstract void stop(Server server) throws Exception;
}
