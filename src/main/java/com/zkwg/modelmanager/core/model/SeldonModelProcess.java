package com.zkwg.modelmanager.core.model;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zkwg.modelmanager.entity.DeployProcessDef;
import com.zkwg.modelmanager.entity.Model;
import com.zkwg.modelmanager.entity.seldon.PredictorSpec;
import com.zkwg.modelmanager.entity.seldon.SeldonDeployment;
import com.zkwg.modelmanager.service.IDeployProcessDefService;
import com.zkwg.modelmanager.service.IModelService;
import com.zkwg.modelmanager.utils.MD5Util;
import com.zkwg.modelmanager.utils.MinioClientUtils;
import com.zkwg.modelmanager.utils.MinioProperties;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

public class SeldonModelProcess extends ModelProcess {

    private static Logger logger = LoggerFactory.getLogger(SeldonModelProcess.class);

    private MinioProperties minioProperties;

    private IModelService modelService;

    private IDeployProcessDefService deployProcessDefService;

    public SeldonModelProcess(MinioProperties minioProperties, IModelService modelService, IDeployProcessDefService deployProcessDefService) {
        this.minioProperties = minioProperties;
        this.modelService = modelService;
        this.deployProcessDefService = deployProcessDefService;
    }

    @Override
    public void process(Model model) throws Exception {
        File file = new File(model.getPath());
        model.setFilename(getModelFileName(model));
        //  1.上传到Minio服务器
        MinioClientUtils.getInstance(minioProperties).putObject(model,file);// 这里的模型名称可能定死
        //  2.保存元数据到数据库
        modelService.insertSelective(model);
    }

    @Override
    public DeployProcessDef generateProcessDef(String template, Model model) throws Exception {
        InputStream is =  SeldonModelProcess.class.getClassLoader().getResourceAsStream(template);
        SeldonDeployment seldonDeployment = Serialization.unmarshal(is, SeldonDeployment.class);
        // 2.设置部署参数
        setDeployParams(model,seldonDeployment);
        // 3.保存到模型流程定义
        return getOrCreateDeployProcessDef(model,Serialization.asJson(seldonDeployment));
    }

    /**
     * 设置部署参数
     * @param model
     * @param seldonDeployment
     */
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

    private String getModelFileName(Model model) throws Exception {

        switch (model.getImplementation()) {
            case "SKLEARN_SERVER" : return "model.joblib";
            case "TENSORFLOW_SERVER": return "saved_model.pd";
            default: throw new RuntimeException("不知名Implementation类型");
        }

    }

}
