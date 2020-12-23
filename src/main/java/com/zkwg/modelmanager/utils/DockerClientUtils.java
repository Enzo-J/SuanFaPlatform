package com.zkwg.modelmanager.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.command.PushImageResultCallback;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


public class DockerClientUtils {

    private static Logger logger = LoggerFactory.getLogger(DockerClientUtils.class);

    private DockerProperties dockerProperties;

    private static DockerClientUtils dockerClientUtils;

    private final static ThreadLocal<DockerClient> threadLocal = new ThreadLocal<DockerClient>();


    private DockerClientUtils(DockerProperties dockerProperties){
        init(dockerProperties);
    }

    private void init(DockerProperties dockerProperties) {
        this.dockerProperties = dockerProperties;
    }

    public static DockerClientUtils getInstance(DockerProperties dockerProperties){
        if(dockerClientUtils == null){
            synchronized (MinioClientUtils.class){
                if(dockerClientUtils == null){
                    dockerClientUtils = new DockerClientUtils(dockerProperties);
                    return dockerClientUtils;
                }
            }
        }
        return dockerClientUtils;
    }


    private DockerClient getConnection(){

        DockerClient client = threadLocal.get();

        if(client == null){
            try {
                DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                                            .withDockerHost(dockerProperties.getDockerUrl())
                                            .build();
                DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                                            .dockerHost(config.getDockerHost())
                                            .build();
                client = DockerClientImpl.getInstance(config,httpClient);

                threadLocal.set(client);
            }catch (Exception e){
                logger.info("连接docker失败",e);
                throw new RuntimeException("连接docker失败",e);
            }
        }

        return client;
    }

    /**
     * 创建镜像
     */
    public String createImage(File file, String tag, BuildImageResultCallback callback){
        try {
            DockerClient client = getConnection();
            return client.buildImageCmd(file).withTag(tag).exec(callback).awaitImageId();
//            client.buildImageCmd(file).withTag(tag).exec(callback).onComplete();
        } catch (Exception e){
            logger.info("创建镜像失败",e);
            throw new RuntimeException("创建镜像失败",e);
        }
    }

    /**
     * 为镜像打Tag
     * @param imageId
     * @param newImageName
     * @param newVersion
     */
    public void createTag(String imageId,String newImageName, String newVersion) {
        try {
            DockerClient client = getConnection();
            client.tagImageCmd(imageId,newImageName,newVersion).exec();
        } catch (Exception e){
            logger.info("创建Tag失败",e);
            throw new RuntimeException("创建Tag失败",e);
        }
    }

    /**
     * 删除镜像
     */
    public void removeImage(String imageId){
        try{
            DockerClient client = getConnection();
            client.removeImageCmd(imageId);
        } catch (Exception e){
            logger.info("推送镜像失败",e);
            throw new RuntimeException("推送镜像失败",e);
        }
    }


    /**
     * 推送镜像
     */
    public void pushImage(String imageName, String tag, PushImageResultCallback callback){
        try{
            AuthConfig authConfig = new AuthConfig()
                                    .withUsername(dockerProperties.getUsername())
                                    .withPassword(dockerProperties.getPassword())
                                    .withRegistryAddress(dockerProperties.getDockerUrl());
            DockerClient client = getConnection();
            client.pushImageCmd(imageName).withTag(tag).withAuthConfig(authConfig).exec(callback).awaitSuccess();
        } catch (Exception e){
            logger.info("推送镜像失败",e);
            throw new RuntimeException("推送镜像失败",e);
        }
    }


}
