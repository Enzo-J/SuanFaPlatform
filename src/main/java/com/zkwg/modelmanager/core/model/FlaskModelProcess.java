package com.zkwg.modelmanager.core.model;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.ImmutableMap;
import com.zkwg.modelmanager.core.ApiDoc;
import com.zkwg.modelmanager.core.BaseContextHandler;
import com.zkwg.modelmanager.entity.DeployProcessDef;
import com.zkwg.modelmanager.entity.Model;
import com.zkwg.modelmanager.service.IDeployProcessDefService;
import com.zkwg.modelmanager.service.IModelService;
import com.zkwg.modelmanager.utils.*;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FlaskModelProcess extends ModelProcess {

    private static Logger logger = LoggerFactory.getLogger(FlaskModelProcess.class);

    private String uploadPath;

    private DockerProperties dockerProperties;

    private MinioProperties minioProperties;

    private IModelService modelService;

    private IDeployProcessDefService deployProcessDefService;

    private ExecutorService executorService = Executors.newFixedThreadPool(20);

    public FlaskModelProcess(DockerProperties dockerProperties,MinioProperties minioProperties, IModelService modelService,IDeployProcessDefService deployProcessDefService) {
        this.uploadPath = minioProperties.getUploadPath();
        this.dockerProperties = dockerProperties;
        this.minioProperties = minioProperties;
        this.modelService = modelService;
        this.deployProcessDefService = deployProcessDefService;
    }

    @Override
    public void process(Model model) throws Exception {

//        MinioClientUtils.getInstance(minioProperties).putObject(model,new File(destPath));
        // 保存元数据到数据库
        modelService.insertSelective(model);// 这里要返回主键
        // 创建线程，打成镜像，上传镜像
        CompletableFuture.runAsync(new DockerThread(BaseContextHandler.getTenant(), dockerProperties,model,modelService), executorService);
    }



    @Override
    public DeployProcessDef generateProcessDef(String template, Model model) throws Exception {

        String temp = IOUtils.toString(SeldonModelProcess.class.getClassLoader().getResourceAsStream(template));
        String[] strArr =  temp.split("---");
        Deployment deployment = Serialization.unmarshal(strArr[0], Deployment.class);
        Service service = Serialization.unmarshal(strArr[1], Service.class);
        // 2.设置部署参数
        setDeployParams(model,deployment,service);
        String deploymentStr = Serialization.asJson(deployment);
        String serviceStr = Serialization.asJson(service);
        // 3.保存到模型流程定义
        return getOrCreateDeployProcessDef(model, deploymentStr + "---" + serviceStr);
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

    private void setDeployParams(Model model, Deployment deployment,Service service) {
        ObjectMeta serviceMeta = service.getMetadata();
        ObjectMeta depMetadata = deployment.getMetadata();
        List<Container> containers = deployment.getSpec().getTemplate().getSpec().getContainers();
        // 设置部署名称
        depMetadata.setName(model.getNameEn() + "-" + depMetadata.getName());
        serviceMeta.setName(model.getNameEn() + "-" + serviceMeta.getName());
        // 设置镜像
        Optional<Container> containerOptional = containers.stream().findFirst();
        Container container = containerOptional.get();
        container.setImage(model.getImage());
        // 设置端口号
        ApiDoc apiDoc = Serialization.unmarshal(model.getApiDoc(), ApiDoc.class);
        Optional<ContainerPort> containerPortOptional = container.getPorts().stream().findFirst();
        ContainerPort containerPort = containerPortOptional.get();
        containerPort.setContainerPort(apiDoc.getServicePort().getPort());
        // 设置服务端口
        IntOrString targetPort = new IntOrString(apiDoc.getServicePort().getPort());
        Optional<ServicePort> optionalServicePort = service.getSpec().getPorts().stream().findFirst();
        ServicePort servicePort = optionalServicePort.get();
        servicePort.setPort(apiDoc.getServicePort().getPort());
        servicePort.setTargetPort(targetPort);
        // 设置访问网关
        String namespace = serviceMeta.getNamespace();
        String name = serviceMeta.getName();
        // 设置predictions
        String mappingName = namespace + name + "_rest_mapping";
        String prefix = "/flask/" + namespace + "/" + name + "/api/v1.0/predictions";
        String serviceStr = name + "." + namespace + ":" + servicePort.getPort();
        String rewritePath = apiDoc.getRoute().getPredictions();
        String ambassadorValue = "---\napiVersion: ambassador/v1\nkind: Mapping\nname: " + mappingName + "\nprefix: " + prefix + " \nrewrite: "+ rewritePath +"\nservice: " + serviceStr + " \ntimeout_ms: 3000\n";
        service.getMetadata().setAnnotations(ImmutableMap.of("getambassador.io/config",ambassadorValue));
        // 设置Doc
        mappingName = namespace + name + "_doc_mapping";
        prefix = "/flask/" + namespace + "/" + name + "/api/v1.0/doc";
        serviceStr = name + "." + namespace + ":" + servicePort.getPort();
        rewritePath = apiDoc.getRoute().getDoc();
        ambassadorValue = ambassadorValue + "---\napiVersion: ambassador/v1\nkind: Mapping\nname: " + mappingName + "\nprefix: " + prefix + " \nrewrite: "+ rewritePath +"\nservice: " + serviceStr + " \ntimeout_ms: 3000\n";
        service.getMetadata().setAnnotations(ImmutableMap.of("getambassador.io/config", ambassadorValue));
    }


    /**
     * 检查是否是zip文件
     * @param file
     */
    private void checkFileType(MultipartFile file) {

    }


}
