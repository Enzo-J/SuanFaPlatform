package com.zkwg.modelmanager.core.model;

import com.zkwg.modelmanager.ModelManagerApplication;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelManagerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ModelTypeEnumTest {

    private static Logger logger = LoggerFactory.getLogger(ModelTypeEnumTest.class);

    @Test
    void match() {
        ModelTypeEnum modelTypeEnum = ModelTypeEnum.match("SKLEARN_SERVER");
        Assert.assertNotNull(modelTypeEnum);
    }
}