package com.zkwg.modelmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.zkwg.modelmanager.core.model.ModelProcess;
import com.zkwg.modelmanager.core.model.ModelTypeEnum;
import com.zkwg.modelmanager.entity.*;
import com.zkwg.modelmanager.entity.seldon.PredictorSpec;
import com.zkwg.modelmanager.entity.seldon.SeldonDeployment;
import com.zkwg.modelmanager.entity.seldon.SeldonDeploymentStatus;
import com.zkwg.modelmanager.mapper.ModelMapper;
import com.zkwg.modelmanager.request.ModelRequest;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.security.AISecurityUser;
import com.zkwg.modelmanager.security.dataScope.DataScope;
import com.zkwg.modelmanager.service.*;
import com.zkwg.modelmanager.service.base.BaseService;
import com.zkwg.modelmanager.utils.MD5Util;
import com.zkwg.modelmanager.utils.MinioClientUtils;
import com.zkwg.modelmanager.utils.MinioProperties;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

@Service
public class ModelServiceImpl extends BaseService<ModelMapper, Model> implements IModelService {

    private static Logger logger = LoggerFactory.getLogger(ModelServiceImpl.class);

    private ModelMapper modelMapper;

    @Autowired
    private MinioProperties minioProperties;

    @Autowired
    private ITrainingService trainingService;

    @Autowired
    private ITrainingModelService trainingModelService;

    @Autowired
    private IDeployProcessDefService deployProcessDefService;

    @Autowired
    private IContainerService containerService;

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.baseMapper = modelMapper;
    }

    @Override
    public R deploy(Model model) throws Exception {
        // 1.加载单模型部署模板；
        ModelTypeEnum modelTypeEnum = ModelTypeEnum.match(model.getImplementation());
        InputStream is =  ModelServiceImpl.class.getClassLoader().getResourceAsStream(modelTypeEnum.template);
        SeldonDeployment seldonDeployment = Serialization.unmarshal(is, SeldonDeployment.class);
        // 2.设置部署参数
        setDeployParams(model,seldonDeployment);
        // 3.保存到模型流程定义
        DeployProcessDef deployProcessDef = getOrCreateDeployProcessDef(model,Serialization.asJson(seldonDeployment));

        return ResultUtil.success(deployProcessDef);
    }

    private void setDeployParams(Model model, SeldonDeployment seldonDeployment) {
        String implementation = model.getImplementation();
        String namespace = implementation.substring(0,implementation.indexOf("_")).toLowerCase()+"-model";
        String name = model.getNameEn();
        // 设置部署内容
        seldonDeployment.getMetadata().setNamespace(namespace);
        seldonDeployment.getMetadata().setName(name);
        // 设置模型
        PredictorSpec[] predictors = seldonDeployment.getSpec().getPredictors();
        predictors[0].getGraph().setModelUri(model.getMinioUrl());
        predictors[0].getGraph().setImplementation(model.getImplementation());
    }

    private byte getModelStatus(SeldonDeploymentStatus seldonDeploymentStatus) {
        if(seldonDeploymentStatus == null){
            return  4; // 超时
        }
        if(seldonDeploymentStatus != null && "Available".equals(seldonDeploymentStatus.getState())){
            return  2; // 已部署
        }
        return 5;// 部署失败
    }

    private Container getContainer(String params) { 
        if(params != null){
            Container container = Serialization.unmarshal(params,Container.class);
            QueryWrapper queryWrapper = new QueryWrapper(new com.zkwg.modelmanager.entity.Container(container.getName()));
            com.zkwg.modelmanager.entity.Container backContainer = containerService.selectOne(queryWrapper);
            Assert.notNull(backContainer,"无法找到分配的容器");
            backContainer.setStatus((byte)2); // 2.已分配
            containerService.updateByPrimaryKeySelective(backContainer);
            return container;
        }
        return  new Container();
    }

    @Override
    public List<Map<Integer, String>> getModelType(int flag) {

        List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();
        if(flag == 1){ // 业务模型类型
            list.add(new HashMap<Integer, String>(){{
                put(1,"税收经济");
                put(2,"税务稽查");
                put(3,"风险管控");
                put(4,"税收征管");
                put(5,"企业服务");
            }});
        }
        if(flag == 2){ // 基础模型类型
            list.add(new HashMap<Integer, String>(){{
                put(10,"自然语言处理");
                put(11,"舆情分析");
                put(12,"知识图谱");
                put(13,"语音技术");
                put(14,"图像技术");
                put(15,"视频技术");
                put(16,"文字识别");
                put(17,"人脸与人体识别");

            }});
        }
        return list;
    }

    @Override
    public void updateStatusByUrl(List<Model> models) {
        modelMapper.updateStatusByUrls(models);
    }

    @Override
    public void updateStatusByUrl(Model model) {
        modelMapper.updateStatusByUrl(model);
    }

    @Override
    public void updateStatusByImage(List<Model> models) {
        modelMapper.updateStatusByImage(models);
    }

    @Override
    public IPage<Model> subscribeList(Integer pageNum, Integer pageSize, ModelRequest modelRequest) {
        return modelMapper.subscribeList(new Page(pageNum, pageSize), modelRequest, new DataScope());
    }

    @Override
    public IPage<Model> publicModelList(Integer pageNum, Integer pageSize, ModelRequest modelRequest) {
        return modelMapper.publicModelList(new Page(pageNum, pageSize), modelRequest);
    }

    @Override
    public IPage<Model> findPage(int pageNum, int pageSize, Wrapper<Model> wrapper) {
        return modelMapper.findPage(new Page(pageNum, pageSize), wrapper, new DataScope());
    }

    @Override
    public void importModel(Integer trainingId, Model model) throws Exception {
        Training training = trainingService.selectByPrimaryKey(trainingId);
        Assert.notNull(training, "无法找到训练作业 " + training);
        LambdaQueryWrapper<TrainingModel> queryWrapper = new QueryWrapper<TrainingModel>().lambda().eq(TrainingModel::getModel_id, training.getModelId());
        TrainingModel trainingModel = trainingModelService.selectOne(queryWrapper);
        //  1.上传到Minio服务器
        MinioClientUtils.getInstance(minioProperties).putObject(model,new ByteArrayInputStream(trainingModel.getModel()));
        //  2.保存元数据到数据库
        modelMapper.insert(model);
    }

    /**
     * 创建部署流程定义
     * @param model
     * @param asJson
     * @return
     */
    private DeployProcessDef getOrCreateDeployProcessDef(Model model, String asJson) {
        logger.info("部署流程图中的SeldonDeployment : ",asJson);
        String deployJsonMd5 = MD5Util.getMD5(asJson);
        DeployProcessDef param = new DeployProcessDef();
        param.setDeployJsonMd5(deployJsonMd5);
        DeployProcessDef result = deployProcessDefService.selectOne(new QueryWrapper<>(param));
        if(result == null){
            DeployProcessDef deployProcessDef = new DeployProcessDef();
            deployProcessDef.setDeployJson(asJson);
            deployProcessDef.setName(model.getName());
            deployProcessDef.setCreatetime(new Date());
            deployProcessDef.setCreator(model.getCreator());
            deployProcessDef.setFlag((byte)1);
            deployProcessDef.setDeployJsonMd5(deployJsonMd5);
            deployProcessDef.setModels(model.getId() + "");
            //
            deployProcessDefService.insertAndGetId(deployProcessDef);
            Assert.notNull(deployProcessDef.getId(),"保存部署流程定义图失败！");
            return deployProcessDef;
        }

        return result;
    }

    /**
     * 设置部署参数
     */
    private void setDeployParams(Model model, Container container, SeldonDeployment seldonDeployment) {
        String implementation = model.getImplementation();
        String namespace = implementation.substring(0,implementation.indexOf("_")).toLowerCase()+"-model";
        String name = model.getNameEn();
        // 设置部署内容
        seldonDeployment.getMetadata().setNamespace(namespace);
        seldonDeployment.getMetadata().setName(name);
        //
        PredictorSpec[] predictors = seldonDeployment.getSpec().getPredictors();
        predictors[0].getGraph().setModelUri(model.getMinioUrl());
        predictors[0].getGraph().setImplementation(model.getImplementation());
        // 设置运行时容器资源
        List<Container> containers = Lists.newArrayList(container);
        predictors[0].getGraph().setName(container.getName());
        predictors[0].getComponentSpecs()[0].getSpec().setContainers(containers);

    }


}
