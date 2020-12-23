package com.zkwg.modelmanager.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zkwg.modelmanager.core.IMessage;
import com.zkwg.modelmanager.core.K8sDeployResult;
import com.zkwg.modelmanager.entity.Server;
import com.zkwg.modelmanager.request.ServerRequest;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.service.base.IBaseService;

import java.util.List;


public interface IServerService extends IBaseService<Server> {


    R stop(Server server) throws InterruptedException, Exception;

    R redeploy(Server server) throws Exception;

    String watchLog(Server server);

    Server create(Server server);

    void redeploy(Server server, String userId);

    void deleteServer(Server server);

    void deploySuccess(IMessage<K8sDeployResult> message);

    void deployError(IMessage<K8sDeployResult> message);

    void deployTimeout(IMessage<K8sDeployResult> message);

    IPage<Server> subscribeList(Integer pageNum, Integer pageSize, ServerRequest serverRequest);

    IPage<Server> publicServerList(Integer pageNum, Integer pageSize, ServerRequest serverRequest);

    IPage<Server> findPage(int pageNum, int pageSize, Wrapper<Server> wrapper);
}
