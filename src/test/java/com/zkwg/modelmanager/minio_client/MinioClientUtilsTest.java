package com.zkwg.modelmanager.minio_client;

import com.zkwg.modelmanager.ModelManagerApplication;
import com.zkwg.modelmanager.utils.MinioClientUtils;
import com.zkwg.modelmanager.utils.MinioProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelManagerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MinioClientUtilsTest {

    private static Logger logger = LoggerFactory.getLogger(MinioClientUtilsTest.class);

    @Autowired
    private MinioProperties minioProperties;

    @BeforeEach
    void setUp() {
//        minioClientUtils = MinioClientUtils.getInstance();
    }

    @Test
    void createBucketIfNotExist() {
        MinioClientUtils.getInstance(minioProperties).createBucketIfNotExist("testBucket");
    }

    @Test
    void putObject() {
        try (FileInputStream fis = new FileInputStream("E:\\项目\\github\\model-manager\\src\\main\\resources\\k8s\\admin.conf")){
            MinioClientUtils.getInstance(minioProperties).putObject("test","2019-09-09--12:12:21/admin.conf",fis);
        } catch (Exception e) {
            logger.debug("put object into bucket Error \n"+e.toString());
        }
    }
}