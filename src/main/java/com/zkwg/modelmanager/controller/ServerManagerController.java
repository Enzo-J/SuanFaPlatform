package com.zkwg.modelmanager.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zkwg.modelmanager.core.server.ServerProcess;
import com.zkwg.modelmanager.core.server.ServerTypeEnum;
import com.zkwg.modelmanager.entity.Model;
import com.zkwg.modelmanager.entity.Role;
import com.zkwg.modelmanager.entity.Server;
import com.zkwg.modelmanager.entity.seldon.SeldonDeployment;
import com.zkwg.modelmanager.request.PageInfoRequest;
import com.zkwg.modelmanager.request.ServerRequest;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.service.IServerService;
import com.zkwg.modelmanager.utils.MD5Util;
import com.zkwg.modelmanager.utils.SecurityUtil;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.client.utils.Serialization;
import okhttp3.*;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/server")
public class ServerManagerController {

    private static Logger logger = LoggerFactory.getLogger(ServerManagerController.class);

    @Value("${web.uploadpath}")
    private String uploadPath;

    @Value("${ambassador.url}")
    private String ambassadorUrl;

    @Autowired
    private IServerService serverService;

//    @Autowired
//    private IIntegrateServerService integrateServerService;

//    @PostMapping("/call/{id}")
//    public R callIntegrateServer(@PathVariable int id, String paramterJson){
//        ListListParameter listListParameter = Serialization.unmarshal(paramterJson,ListListParameter.class);
//        return ResultUtil.success(integrateServerService.call(id,paramterJson));
//    }

//    /**
//     * 上传插件
//     */
//    @Deprecated
//    @PostMapping("/plugin")
//    public R plugin(@RequestParam("file") MultipartFile file) throws Exception {
//
//        if (file.isEmpty()) {
//            return ResultUtil.failure(null,"上传失败，请选择文件");
//        }
//        // 1.保存插件到指定目录
//        String basePath = uploadPath + "static/pluginLib/";
//        logger.info("basePath : " + basePath);
//        String fileName = file.getOriginalFilename();
//        File dest = new File(basePath + "/" + fileName);
//        try {
//            //判断文件父目录是否存在
//            if (!dest.getParentFile().exists()) {
//                logger.info("dest.getParentFile() : "+dest.getParentFile().getParent());
//                dest.getParentFile().mkdirs();
//            }
//
//            file.transferTo(dest);
//            logger.info("上传成功");
//        } catch (IOException e) {
//            logger.error(e.toString(), e);
//            return ResultUtil.failure(null,"上传失败");
//        }
//
//        // 2.生成第三方服务
//        integrateServerService.create(new Plugin(dest.getAbsolutePath(),fileName));
//
//        return ResultUtil.success();
//    }

    /**
     * 查找模型
     */
    @PostMapping("/search")
    public R<T> search(@RequestBody ServerRequest serverRequest){

        LambdaQueryWrapper<Server> queryWrapper = new QueryWrapper<Server>()
                .lambda()
                .eq(serverRequest.getBusinessType() != null, Server::getBusinessType, serverRequest.getBusinessType())
                .like(StrUtil.isNotBlank(serverRequest.getName()), Server::getName, serverRequest.getName())
                .eq(Server::getIsDelete, 0)
                .orderByDesc(Server::getCreatetime);
        IPage<Server> page = serverService.findPage(serverRequest.getPageNum(), serverRequest.getPageSize(),queryWrapper);

        return ResultUtil.success(page);
    }

    /**
     * 创建服务实例
     */
    @PostMapping("/create")
    public R create(@RequestBody ServerRequest serverRequest) throws Exception {
        Server server = deserializeServerRequest(serverRequest);
        server.setCreator(SecurityUtil.getUser().getUserId());
        return ResultUtil.success(serverService.create(server));
    }

    /**
     * 测试服务实例
     */
    @PostMapping("/test/{id}")
    public R test(@PathVariable int id) throws Exception {
        Server server = serverService.selectByPrimaryKey(id);

        ServerTypeEnum serverlTypeEnum = ServerTypeEnum.match(server.getImplementation());
        ServerProcess serverProcess = serverlTypeEnum.serverProcess;
        String v = serverProcess.test(server);
        return ResultUtil.success(ambassadorUrl + v);
    }

    /**
     * 测试服务实例
     */
    @PostMapping("/sendOcr")
    public R sendOcr(@RequestParam("url") String url, @RequestParam("params") String params) throws Exception {
        OkHttpClient httpClient = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8");
        String post = params;
        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(mediaType, post);
        Request request = new Request.Builder()
                .post(requestBody)
                .url(url)
                .build();
        Response response = httpClient.newCall(request).execute();
        logger.info("返回值：" + response.body().string());
        return ResultUtil.success(response.body().string());
    }

    /**
     * 身份证识别
     */
    @PostMapping("/orc/id")
    public R orcID(String base64Img) throws Exception {

        Assert.notNull(base64Img,"base64Img 为空！");

        OkHttpClient httpClient = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
        String post = "{\"img\":\""+ base64Img +"\"}";
        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(mediaType, post);
        Request request = new Request.Builder()
                .post(requestBody)
                .url("http://localhost:5005/orc/predict")
                .build();
        Response response = httpClient.newCall(request).execute();
//        logger.info(response.body().string());
        return ResultUtil.success(response.body().string());
    }

    @PostMapping("/orc/idImg")
    public R orcIDImg(@RequestParam("file") MultipartFile file) throws Exception {

        if (file.isEmpty()) {
            return ResultUtil.failure(null,"上传失败，请选择文件");
        }

        OkHttpClient client = new OkHttpClient();
        okhttp3.RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("img", file.getName(),
                        okhttp3.RequestBody.create(MediaType.parse("multipart/form-data"), file.getBytes()))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + UUID.randomUUID())
                .url("http://localhost:5005/orc/predict")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return ResultUtil.success(response.body().string());
    }

    /**
     * 测试服务实例
     */
    @PostMapping("/send")
    public R test(@RequestParam("url") String url, @RequestParam("params") String params) throws Exception {
        OkHttpClient httpClient = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
        String post = params;
        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(mediaType, post);
        Request request = new Request.Builder()
                .post(requestBody)
                .url(url)
                .build();
        Response response = httpClient.newCall(request).execute();
//        logger.info("返回值：" + response.body().string());
        return ResultUtil.success(response.body().string());
    }

    /**
     * 下线服务
     */
    @RequestMapping("/offline/{id}")
    public R offline(@PathVariable int id) throws Exception {
        //TODO checkModelStatus
        Server server = serverService.selectByPrimaryKey(id);
        Assert.notNull(server,"服务不存在！");
        //TODO 从AI市场屏蔽,状态没有调好
        server.setIsPublish((byte) 0);
        serverService.updateByPrimaryKeySelective(server);
        return ResultUtil.success(null);
    }

    /**
     * 停止服务
     */
    @RequestMapping("/stop/{id}")
    public R stop(@PathVariable int id) throws Exception {
        Server server = serverService.selectByPrimaryKey(id);
//        Server server = checkServerStatus(id,Operator.STOP);
        return serverService.stop(server);
    }

    /**
     * 重新部署模型
     */
    @RequestMapping("/redeploy/{id}")
    public R redeploy(@PathVariable int id) throws Exception {
        //TODO checkServerStatus(id,0);
        Server server = serverService.selectByPrimaryKey(id);
        return serverService.redeploy(server);
    }

    /**
     * 重新部署模型 (使用websocket推送消息)
     */
    @RequestMapping("/redeploy/{id}/{userId}")
    public R redeploy(@PathVariable int id,@PathVariable String userId) throws Exception {
        Server server = serverService.selectByPrimaryKey(id);
        serverService.redeploy(server,userId);
        return ResultUtil.success();
    }

    /**
     * 发布模型  将模型发布到AI市场
     */
    @RequestMapping("/publish/{id}")
    public R publish(@PathVariable int id,@RequestBody Server param) {
        // checkServerStatus(id,0);
        Server server = serverService.selectByPrimaryKey(id);
        server.setIsPublish((byte)1);
        server.setPicture(param.getPicture());
        server.setSummarize(param.getSummarize());
        server.setParamsDesc(param.getParamsDesc());
        server.setDataFormat(param.getDataFormat());
        server.setPrinciple(param.getPrinciple());
        serverService.updateByPrimaryKeySelective(server);
        return ResultUtil.success();
    }

    /**
     * 查看日志
     */
    @RequestMapping("/log/{id}")
    public R watchLog(@PathVariable int id) throws Exception {
        Server server = checkServerStatus(id,Operator.WATCH_LOG);
        return ResultUtil.success(serverService.watchLog(server));
    }

    /**
     * 删除模型
     */
    @RequestMapping("/delete/{id}")
    public R delete(@PathVariable int id){
        Server server = checkServerStatus(id,Operator.DELETE);
        serverService.deleteServer(server);
        return ResultUtil.success();
    }

    /**
     * 服务详情
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable int id){
        Server server = serverService.selectByPrimaryKey(id);
        return ResultUtil.success(server);
    }

//    @PostMapping("/update/{id:[0-9]*}")
//    public R<T> update(@PathVariable("id") int id){
////        checkServerStatus(id,0);
//        return ResultUtil.success();
//    }

    /**
     * 订阅服务列表
     */
    @RequestMapping("/subscribe/list")
    public R subscribeList(@RequestBody ServerRequest serverRequest){
        serverRequest.setUserId(SecurityUtil.getUser().getUserId());
        IPage<Server> servers = serverService.subscribeList(serverRequest.getPageNum(), serverRequest.getPageSize(), serverRequest);
        return ResultUtil.success(servers);
    }

    /**
     * 修改查看数
     */
    @RequestMapping("/view/{id}")
    public R view(@PathVariable int id){
        Server server = serverService.selectByPrimaryKey(id);
        server.setViewNum(server.getViewNum() == null ? 1 : server.getViewNum() + 1);
        serverService.updateByPrimaryKeySelective(server);
        return ResultUtil.success();
    }

    /**
     * 根据状态判定操作是否可以执行
     * @param id 资源池ID
     * @param operator 检测操作：1.删除
     */
    private Server checkServerStatus(int id,Operator operator) {

        Server server = serverService.selectByPrimaryKey(id);
        Assert.notNull(server,"服务不存在！");

        byte status = server.getStatus();
        // 1:正在运行 2：准备停止 3：停止  4.运行失败  5：发布 6：超时  7.创建
        switch (operator) {
            case CREATE:
                break;
            case DELETE:
                if(status == 1 || status == 8){ throw new RuntimeException("服务未停止不能删除！"); }
                break;
            case WATCH_LOG:
                if (status != 1) { throw new RuntimeException("服务未运行不能看日志！"); }
                break;
            case STOP:
                if (status != 1) { throw new RuntimeException("服务未运行！"); }
                break;
        }

        return server;
    }


    /**
     * 解析serverRequest参数
     * @param serverRequest
     * @return
     */
    private Server deserializeServerRequest(ServerRequest serverRequest){
//        checkServerJson(serverRequest.getServerJson());
        Server server = new Server();
        BeanUtils.copyProperties(serverRequest,server);
        server.setServerJsonMd5("");
        return server;
    }

    private void checkServerJson(String serverJson) {
        SeldonDeployment seldonDeployment = Serialization.unmarshal(serverJson,SeldonDeployment.class);
    }

    private enum Operator {
        CREATE,
        DELETE,
        UPDATE,
        MONITOR,
        WATCH_LOG,
        STOP,
        REDEPLOY
    }
}
