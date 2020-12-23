package com.zkwg.modelmanager.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zkwg.modelmanager.core.model.ModelProcess;
import com.zkwg.modelmanager.core.model.ModelTypeEnum;
import com.zkwg.modelmanager.entity.Container;
import com.zkwg.modelmanager.entity.Model;
import com.zkwg.modelmanager.entity.Server;
import com.zkwg.modelmanager.exception.type.ModelExceptionEnum;
import com.zkwg.modelmanager.request.ModelRequest;
import com.zkwg.modelmanager.request.PageInfoRequest;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.service.IModelService;
import com.zkwg.modelmanager.utils.*;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@RestController
@RequestMapping("/model")
public class ModelManagerController {

    private static Logger logger = LoggerFactory.getLogger(ModelManagerController.class);

    @Autowired
    private MinioProperties minioProperties;

    @Autowired
    private K8sProperties k8sProperties;

    @Autowired
    private IModelService modelService;

    /**
     * 导入模型   手动导入镜像后，创建模型接口
     * @Validated({ModelRequest.SaveModel.class})
     */
    @PostMapping("/create")
    public R importModel(@RequestParam("file") MultipartFile file, ModelRequest modelRequest) throws Exception {

        Model model = deserializeModelRequest(modelRequest);
        if (file.isEmpty()) { return ResultUtil.failure(null,"上传失败，请选择文件"); }
        // 模型导入处理
        String apiDoc = IOUtils.toString(file.getInputStream());
        // 上传压缩包到Minio服务器
        model.setApiDoc(apiDoc);
        model.setStatus((byte) 1);
        model.setFilename(file.getName());
        modelService.insertSelective(model);
        return ResultUtil.success(model);
    }

    /**
     *
     * @param modelRequest
     * @return
     * @throws Exception
     */
    @PostMapping("/import")
    public R importModel(ModelRequest modelRequest) throws Exception {
        Model model = deserializeModelRequest(modelRequest);
        // 模型导入处理
        ModelTypeEnum modelTypeEnum = ModelTypeEnum.match(model.getImplementation());
        ModelProcess modelProcess = modelTypeEnum.modelProcess;
        modelProcess.process(model);
        return ResultUtil.success();
    }

    /**
     * 导入模型
     * @Validated({ModelRequest.SaveModel.class})
     */
    @PostMapping("/import/{trainingId}")
    public R importModel(@PathVariable Integer trainingId,@RequestBody ModelRequest modelRequest) throws Exception {
        Model model = deserializeModelRequest(modelRequest);
        modelService.importModel(trainingId, model);
        return ResultUtil.success();
    }

    /**
     * 导入模型  训练结果导入
     */
    @PostMapping("/importFromTrain")
    public R importFromTrain(@Validated({ModelRequest.SaveModel.class}) ModelRequest modelRequest){
        Model model = deserializeModelRequest(modelRequest);
        modelService.insertSelective(model);
        return ResultUtil.success(null);
    }


    /**
     * 部署服务
     * @param id 模型ID
     * @return
     * @throws Exception
     */
    @PostMapping("/deploy/{id}")
    public R deploy(@PathVariable int id) throws Exception {
        // checkResourcePoolStatus(id,4);
        // 查找模型
        Model model = modelService.selectByPrimaryKey(id);
        ModelExceptionEnum.Model_Not_Exsist.assertNotNull(model);
        ModelTypeEnum modelTypeEnum = ModelTypeEnum.match(model.getImplementation());
        ModelProcess modelProcess = modelTypeEnum.modelProcess;
        // 单模型部署；生成模型部署定义
       return ResultUtil.success(modelProcess.generateProcessDef(modelTypeEnum.template,model));
    }

    /**
     * 查找模型
     * @Validated({ModelRequest.ListModel.class})
     */
    @PostMapping("/search")
    public R<T> search(@RequestBody ModelRequest modelRequest){

        LambdaQueryWrapper<Model> queryWrapper = new QueryWrapper<Model>()
                                                .lambda()
                                                .eq(modelRequest.getBusinessType() != null,Model::getBusinessType, modelRequest.getBusinessType())
                                                .like(StrUtil.isNotBlank(modelRequest.getName()), Model::getName, modelRequest.getName())
                                                .eq(Model::getIsDelete, 0)
                                                .orderByDesc(Model::getCreatetime);
        IPage<Model> page = modelService.findPage(modelRequest.getPageNum(), modelRequest.getPageSize(),queryWrapper);

        return ResultUtil.success(page);
    }

    /**
     * 下线模型
     */
    @RequestMapping("/offline/{id}")
    public R offline(@PathVariable int id) throws Exception {
        //TODO checkModelStatus
        Model model = modelService.selectByPrimaryKey(id);
        Assert.notNull(model,"模型不存在！");
        model.setIsPublish((byte) 0);
        modelService.updateByPrimaryKeySelective(model);
        return ResultUtil.success(null);
    }

    /**
     *TODO 调用模型--测试一下
     */
    @RequestMapping("/call/{id}")
    public R call(@PathVariable int id){
        // checkResourcePoolStatus(id,4);
        return ResultUtil.success();
    }

    /**
     * 发布模型
     */
    @RequestMapping("/publish/{id}")
    public R publish(@PathVariable int id, @RequestBody Model param){
        // checkResourcePoolStatus(id,4);
        Model model = modelService.selectByPrimaryKey(id);
        Assert.notNull(model,"模型不存在！");
        model.setIsPublish((byte)1);
        model.setPicture(param.getPicture());
        model.setSummarize(param.getSummarize());
        model.setParamsDesc(param.getParamsDesc());
        model.setDataFormat(param.getDataFormat());
        model.setPrinciple(param.getPrinciple());
        modelService.updateByPrimaryKeySelective(model);
        return ResultUtil.success();
    }

    /**
     * 删除模型
     */
    @RequestMapping("/delete/{id}")
    public R delete(@PathVariable int id){
        Model model = checkModelStatus(id,Operator.DELETE);
        model.setIsDelete((byte)1);
        modelService.updateByPrimaryKeySelective(model);
        return ResultUtil.success();
    }

    @PostMapping("/update/{id:[0-9]*}")
    public R<T> update(@PathVariable("id") int id,@RequestBody ModelRequest modelRequest){
//        checkResourcePoolStatus(id,4);
        //修改模型信息
        Model model = deserializeModelRequest(modelRequest);
        model.setId(id);
        modelService.updateByPrimaryKeySelective(model);
        return ResultUtil.success();
    }

    /**
     * 订阅模型列表
     */
    @RequestMapping("/subscribe/list")
    public R subscribeList(@RequestBody ModelRequest modelRequest){
        modelRequest.setUserId(SecurityUtil.getUser().getUserId());
        IPage<Model> models = modelService.subscribeList(modelRequest.getPageNum(), modelRequest.getPageSize(), modelRequest);
        return ResultUtil.success(models);
    }

    /**
     * 模型详情
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable int id){
        Model model = modelService.selectByPrimaryKey(id);
        return ResultUtil.success(model);
    }


    @GetMapping("/download/{id:[0-9]*}")
    public String download(@PathVariable("id") int id, HttpServletResponse response) throws Exception {
//        checkResourcePoolStatus(id,4);
        Model model = modelService.selectByPrimaryKey(id);
        Assert.notNull(model,"模型不存在！");
        //
        String minioUrl = model.getMinioUrl();
        String filename = model.getFilename();
        String[] urlArr = minioUrl.split("/");
        String bucketName =  urlArr[2];
        String objectName = urlArr[3]+"/"+filename;

        response.setContentType("application/force-download");// 设置强制下载不打开
        response.addHeader("Content-Disposition", "attachment;fileName=" + filename);// 设置文件名
        try (InputStream is = MinioClientUtils.getInstance(minioProperties).loadObject(bucketName,objectName);
             BufferedInputStream bis = new BufferedInputStream(is);
             OutputStream os = response.getOutputStream();){

            byte[] buffer = new byte[1024];
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        }

        return "下载成功";
    }


    /**
     * 根据状态判定操作是否可以执行
     * @param id 资源池ID
     * @param operator 检测操作：1.删除
     */
    private Model checkModelStatus(int id,Operator operator) {

        Model model = modelService.selectByPrimaryKey(id);
        Assert.notNull(model,"模型不存在！");

        byte status = model.getStatus();
        // 1：创建  2：部署  3：部署失败 4：超时
        switch (operator) {
            case CREATE:
                break;
            case DELETE:
                if (status == 2) {
                    throw new RuntimeException("模型已关联部署流程不能删除！");
                }
                break;
        }
        return model;
    }


    /**
     * 解析modelRequest参数
     * @param modelRequest
     * @return
     */
    private Model deserializeModelRequest(ModelRequest modelRequest){

        Model model = new Model();
        BeanUtils.copyProperties(modelRequest,model);

        String implementation =  StrUtil.isEmpty(modelRequest.getImplementation()) ? "training" : modelRequest.getImplementation();
        String bucketname = getBucketName(implementation);
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd--HH:mm:ss"));

        model.setMinioUrl("s3://"+bucketname+"-model"+"/"+dateTime);
        model.setNameEn(PingYinTools.getPinYinHeadChar(model.getName()));
        model.setCreatetime(new Date());
        model.setVersion("v1.0");
        model.setCreator(SecurityUtil.getUser().getUserId());
        model.setStatus((byte) 1);// 创建
        model.setIsDelete((byte) 0);
        model.setFilename("model.joblib");
        model.setCreator(SecurityUtil.getUser().getUserId());
        return model;
    }

    private String getBucketName(String implementation) {
        return implementation.indexOf("_") > 0 ?
               implementation.substring(0,implementation.indexOf("_")).toLowerCase() :
               implementation.toLowerCase();
    }

    private enum Operator {
        CREATE,
        DELETE,
        UPDATE,
        MONITOR
    }

}
