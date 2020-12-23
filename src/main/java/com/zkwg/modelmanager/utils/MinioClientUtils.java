package com.zkwg.modelmanager.utils;

import com.zkwg.modelmanager.entity.Model;
import com.zkwg.modelmanager.exception.type.MinioClientExceptionEnum;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinioClientUtils {

    private static Logger logger = LoggerFactory.getLogger(MinioClientUtils.class);

    private static MinioClientUtils minioClientUtils;

    private MinioProperties minioProperties;

    private MinioClient minioClient;

    private MinioClientUtils(){
        init(minioProperties);
    }

    private MinioClientUtils(MinioProperties minioProperties){
        init(minioProperties);
    }

    private void init(MinioProperties minioProperties) {
        try {
            minioClient = new MinioClient(
                            minioProperties.getEndpoint(),
                            minioProperties.getAccessKey(),
                            minioProperties.getSecretKey());
            logger.info("create MinioClient success!");
        }  catch ( Exception e) {
            logger.error("create MinioClient Error",e);
        }
    }

    public static MinioClientUtils getInstance(MinioProperties minioProperties){
        if(minioClientUtils == null){
            synchronized (MinioClientUtils.class){
                if(minioClientUtils == null){
                    minioClientUtils = new MinioClientUtils(minioProperties);
                    return minioClientUtils;
                }
            }
        }
        return minioClientUtils;
    }

    /**
     * 创建桶
     */
    public MinioClientUtils createBucketIfNotExist(String bucketName){
        try {
            // Create bucket 'my-bucketname' if it doesn`t exist.
            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(bucketName);
            if(isExist) {
                logger.info("Bucket already exists.");
            } else {
                minioClient.makeBucket(bucketName);
            }

        } catch (Exception e){
            logger.info("create bucket Error!",e);
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * 上传文件到Minio服务器
     */
    public MinioClientUtils putObject(String bucketName, String objectName, InputStream inputStream, PutObjectOptions putObjectOptions){
        try {
            // 創建bucket如果不存在
            createBucketIfNotExist(bucketName);
            // 使用putObject上传一个文件到存储桶中。
            minioClient.putObject(bucketName, objectName, inputStream,putObjectOptions);
        } catch (Exception e){
            logger.info("put object into bucket Error!"+e.toString());
            MinioClientExceptionEnum.Minio_Put_Object_Error.throwException();
        }
        return this;
    }

    public MinioClientUtils putObject(String bucketName, String objectName, InputStream inputStream) throws Exception {
        try(InputStream is = inputStream) {
            return putObject(bucketName, objectName, is,new PutObjectOptions(is.available(), -1));
        }
    }

    public MinioClientUtils putObject(Model model, File file) throws Exception {

        String url = model.getMinioUrl();
        String objectName = model.getFilename();
        try(InputStream is = new FileInputStream(file)) {

            String[] strArr = url.split("//");

            String[] uri = strArr[1].split("/");

            String bucketName = uri[0];

            return putObject(bucketName, uri[1] + "/" + objectName, is,new PutObjectOptions(is.available(), -1));

        }
    }


    public MinioClientUtils putObject(Model model, MultipartFile file) throws Exception {

        String url = model.getMinioUrl();
        String objectName = model.getFilename();
        try(InputStream is = file.getInputStream()) {

            String[] strArr = url.split("//");

            String[] uri = strArr[1].split("/");

            String bucketName = uri[0];

            return putObject(bucketName, uri[1] + "/" + objectName, is,new PutObjectOptions(is.available(), -1));

        }
    }

    /**
     * 刪除對象
     */
    public MinioClientUtils removeObject(String bucketName, String objectName){
        try{
            minioClient.removeObject(bucketName,objectName);
        } catch (Exception e) {
            logger.debug("removeObject Error");
        }
        return this;
    }

    /**
     * 下载對象
     */
    public InputStream loadObject(String bucketName, String objectName){
        try{
            return minioClient.getObject(bucketName,objectName);
        } catch (Exception e) {
            logger.debug("loadObject Error",e);
            throw new RuntimeException("loadObject Error");
        }
    }

    public MinioClientUtils putObject(Model model, ByteArrayInputStream byteArrayInputStream) throws IOException {
        String url = model.getMinioUrl();
        String objectName = model.getFilename();
        try(InputStream is = byteArrayInputStream) {

            String[] strArr = url.split("//");

            String[] uri = strArr[1].split("/");

            String bucketName = uri[0];

            return putObject(bucketName, uri[1] + "/" + objectName, is,new PutObjectOptions(is.available(), -1));

        }
    }
}
