package com.zkwg.modelmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zkwg.modelmanager.entity.Model;
import com.zkwg.modelmanager.entity.Server;
import com.zkwg.modelmanager.entity.Subscribe;
import com.zkwg.modelmanager.mapper.SubscribeMapper;
import com.zkwg.modelmanager.security.AISecurityUser;
import com.zkwg.modelmanager.service.IModelService;
import com.zkwg.modelmanager.service.IServerService;
import com.zkwg.modelmanager.service.ISubscribeService;
import com.zkwg.modelmanager.service.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.jws.WebParam;
import java.time.LocalDateTime;


@Service
public class SubscribeServiceImpl extends BaseService<SubscribeMapper, Subscribe> implements ISubscribeService {

    private static Logger logger = LoggerFactory.getLogger(SubscribeServiceImpl.class);

    @Autowired
    private IServerService serverService;

    @Autowired
    private IModelService modelService;

    private SubscribeMapper subscribeMapper;

    @Autowired
    public void setModelMapper(SubscribeMapper subscribeMapper) {
        this.subscribeMapper = subscribeMapper;
        this.baseMapper = subscribeMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void subscribe(Integer id, int type, AISecurityUser user) {
        Subscribe subscribe = createSubscribe(id,type,user);
        subscribeMapper.insert(subscribe);
        // 修改订阅数
        if(type == 3) {
            Server server = serverService.selectByPrimaryKey(id);
            server.setSubscribeNum(server.getSubscribeNum() == null ? 1 : server.getSubscribeNum() + 1);
            serverService.updateByPrimaryKeySelective(server);
        }
        if(type == 2) {
            Model model = modelService.selectByPrimaryKey(id);
            model.setSubscribeNum(model.getSubscribeNum() == null ? 1 : model.getSubscribeNum() + 1);
            modelService.updateByPrimaryKeySelective(model);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unsubscribe(Integer targetId, int type, AISecurityUser user) {
        LambdaQueryWrapper<Subscribe> queryWrapper = new QueryWrapper<Subscribe>()
                                                    .lambda()
                                                    .eq(Subscribe::getCreator, user.getUserId())
                                                    .eq(Subscribe::getTargetId, targetId)
                                                    .eq(Subscribe::getTargetType, type)
                                                    .eq(Subscribe::getFlag, 1);
        Subscribe subscribe = subscribeMapper.selectOne(queryWrapper);
        Assert.notNull(subscribe, "没有订阅该资源");
        subscribe.setFlag(0);
        subscribe.setCancelTime(LocalDateTime.now());
        subscribeMapper.updateById(subscribe);
        // 修改订阅数
        if(type == 3) {
            Server server = serverService.selectByPrimaryKey(targetId);
            server.setSubscribeNum(server.getSubscribeNum() == null || server.getSubscribeNum() <= 1 ? 0 : server.getSubscribeNum() - 1);
            serverService.updateByPrimaryKeySelective(server);
        }
        if(type == 2) {
            Model model = modelService.selectByPrimaryKey(targetId);
            model.setSubscribeNum(model.getSubscribeNum() == null || model.getSubscribeNum() <= 1 ? 0 : model.getSubscribeNum() - 1);
            modelService.updateByPrimaryKeySelective(model);
        }
    }

    private Subscribe createSubscribe(Integer id, int type, AISecurityUser user) {
        Subscribe subscribe = new Subscribe();
        subscribe.setTargetId(id);
        subscribe.setTargetType(type);
        subscribe.setCreateTime(LocalDateTime.now());
        subscribe.setCreator(user.getUserId());
        subscribe.setFlag(1);
        return  subscribe;
    }
}
