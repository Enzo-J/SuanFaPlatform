package com.zkwg.modelmanager.core.model;

import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.core.command.PushImageResultCallback;
import com.zkwg.modelmanager.core.BaseContextHandler;
import com.zkwg.modelmanager.entity.Model;
import com.zkwg.modelmanager.service.IModelService;
import com.zkwg.modelmanager.utils.DockerClientUtils;
import com.zkwg.modelmanager.utils.DockerProperties;
import com.zkwg.modelmanager.utils.ZipUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;

public class DockerThread extends Thread {

    private static Logger logger = LoggerFactory.getLogger(DockerThread.class);

    private Integer tenantId;

//    private String destPath;

    private DockerProperties dockerProperties;

    private Model model;

    private IModelService modelService;

    public DockerThread(Integer tenantId, DockerProperties dockerProperties, Model model, IModelService modelService) {
        this.tenantId = tenantId;
//        this.destPath = destPath;
        this.dockerProperties = dockerProperties;
        this.model = model;
        this.modelService = modelService;
    }

    @Override
    public void run() {

        String imageIdCopy = "";
        final String repository = "py_flask_" + System.currentTimeMillis();
        final Integer tenantId = this.tenantId;
        BaseContextHandler.setTenant(tenantId);
        logger.info(" tenantId 等于 {}", tenantId);
        try {
            /*********解压文件************/
            File projectFile = new File(model.getPath());
            // 拷贝到服务器文件系统
            String fileName = projectFile.getName();
            String basePath = projectFile.getParent() + File.separator + fileName.substring(0, fileName.indexOf("-"));
            // 解压
            basePath = ZipUtils.unZipFiles(projectFile, basePath);
            // 解析接口文件
            String apiDoc = IOUtils.toString(new FileInputStream(new File(basePath + "/api.json")));
            // 上传压缩包到Minio服务器
            model.setApiDoc(apiDoc);
            model.setStatus((byte) -1); // 就绪状态，因为镜像未打成，或未上传镜像
            model.setFilename(projectFile.getName());
            modelService.updateByPrimaryKeySelective(model);
            /*********解压文件************/
            // 找到Dockerfile
            String dockerFile = basePath + File.separator + "Dockerfile";
            File file = new File(dockerFile);

            final String imageId = DockerClientUtils.getInstance(dockerProperties).createImage(file, repository, new BuildImageResultCallback() {
                @Override
                public void onNext(BuildResponseItem item) {
                    super.onNext(item);
                    logger.info(item.toString());
                }
            });

            imageIdCopy = imageId;

            DockerClientUtils.getInstance(dockerProperties).createTag(imageId,dockerProperties.getRepositoryPath(repository),imageId);

            DockerClientUtils.getInstance(dockerProperties).pushImage(dockerProperties.getRepositoryPath(repository), imageId, new PushImageResultCallback() {
                @Override
                public void onNext(PushResponseItem item) {
                    super.onNext(item);
                    logger.info(item.toString());
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    BaseContextHandler.setTenant(tenantId);
                    logger.info("镜像上传完成。。。。");
                    logger.info("模型   {}" + model.toString());
                    model.setImage(dockerProperties.getRepositoryPath(repository) + ":" + imageId);
                    model.setStatus((byte) 1);
                    modelService.updateByPrimaryKeySelective(model);
                    // clear
                    DockerClientUtils.getInstance(dockerProperties).removeImage(imageId);
                }
            });

        } catch (Exception e) {
            model.setStatus((byte) 5);
            modelService.updateByPrimaryKeySelective(model);
            DockerClientUtils.getInstance(dockerProperties).removeImage(imageIdCopy);
            throw new RuntimeException("docker打包镜像出现异常！", e);
        }
    }


}
