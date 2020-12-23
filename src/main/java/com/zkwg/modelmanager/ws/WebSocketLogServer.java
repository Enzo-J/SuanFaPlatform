package com.zkwg.modelmanager.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.zkwg.modelmanager.k8s.SeldonDeploymentWatcher;
import com.zkwg.modelmanager.security.AISecurityUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint("/ai-ws-server/logs/{userId}")
@Component
public class WebSocketLogServer {

    private static Logger logger = LoggerFactory.getLogger(SeldonDeploymentWatcher.class);
    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String, WebSocketLogServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId="";

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId) {
        this.session = session;
        this.userId=userId;
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            webSocketMap.put(userId,this);
            //加入set中
        }else{
            webSocketMap.put(userId,this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }

        logger.info("用户连接:"+userId+",当前在线人数为:" + getOnlineCount());

        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            logger.error("用户:"+userId+",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        logger.info("用户退出:"+userId+",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("用户消息:"+userId+",报文:"+message);
        //可以群发消息
        //消息保存到数据库、redis
        if(StringUtils.isNotBlank(message)){
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                //追加发送人(防止串改)
                jsonObject.put("fromUserId",this.userId);
                String toUserId=jsonObject.getString("toUserId");
                //传送给对应toUserId用户的websocket
                if(StringUtils.isNotBlank(toUserId)&&webSocketMap.containsKey(toUserId)){
                    webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
                }else{
                    logger.error("请求的userId:"+toUserId+"不在该服务器上");
                    //否则不在这个服务器上，发送到mysql或者redis
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 发送自定义消息
     * */
    public void sendInfo(String message,@PathParam("userId") String userId) {
        logger.info("发送消息到:"+userId+"，报文:"+message);
        try {
            if(StringUtils.isNotBlank(userId)&&webSocketMap.containsKey(userId)){
                webSocketMap.get(userId).sendMessage(message);
            }else{
                logger.error("用户"+userId+",不在线！");
            }
        } catch (Exception e) {
            logger.error("发送自定义消息异常",e);
            throw new RuntimeException("发送自定义消息异常",e);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketLogServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketLogServer.onlineCount--;
    }

    public void sendAccessLogs(AISecurityUser securityUser, HttpServletRequest request) throws IOException {
        //
        Integer tenantId = securityUser.getTenantId();
        Integer userId = securityUser.getUserId();
        String username = securityUser.getUsername();
        String uri = request.getRequestURI();
        //
//        if(webSocketMap.contains(tenantId + "-" +userId)) {
        String message = username + "正在使用" + getFunctionName(uri);
        Enumeration<String> enumeration = webSocketMap.keys();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            if((tenantId+"").equals(key.split("-")[0])) {
                webSocketMap.get(key).sendMessage(message);
            }
        }
//        }

    }

    private String getFunctionName(String uri) {
        Map<String,String> map = new HashMap<>();
        map.put("/user","用户管理功能");
        map.put("/dataset","数据集管理功能");
        map.put("/datasource","数据源管理功能");
        map.put("/container","容器管理功能");
        map.put("/algorithm","算法管理功能");
        map.put("/training","训练管理功能");
        map.put("/model","模型管理功能");
        map.put("/server","服务管理功能");
        map.put("/market","AI市场功能");

        for(Map.Entry<String,String> entry : map.entrySet()) {
            if(uri.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "";
    }
}
