package com.zkwg.modelmanager.utils;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.core.command.PushImageResultCallback;
import com.zkwg.modelmanager.ModelManagerApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelManagerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DockerClientUtilsTest {

    private static Logger logger = LoggerFactory.getLogger(DockerClientUtilsTest.class);

    @Autowired
    public DockerProperties dockerProperties;

    @Test
    public void createImageTest() throws Exception {

        File file = new File("C:\\Users\\Messi\\Desktop\\code-master\\docker-package\\docker-package\\target\\classes\\code\\Dockerfile");
        String tag = "py_flask_" + System.currentTimeMillis();
        String imageId = DockerClientUtils.getInstance(dockerProperties).createImage(file, tag, new BuildImageResultCallback() {
            @Override
            public void onNext(BuildResponseItem item) {
                super.onNext(item);
                logger.debug(item.toString());
            }
        });

        DockerClientUtils.getInstance(dockerProperties).createTag(imageId,dockerProperties.getRepositoryPath(tag),"");

//        DockerClientUtils.getInstance(dockerProperties).pushImage(dockerProperties.getRepositoryPath(tag), new PushImageResultCallback() {
//            @Override
//            public void onNext(PushResponseItem item) {
//                super.onNext(item);
//                logger.debug(item.toString());
//            }
//
//            @Override
//            public void onComplete() {
//                super.onComplete();
//                logger.info("onComplete");
//            }
//        });

    }


}