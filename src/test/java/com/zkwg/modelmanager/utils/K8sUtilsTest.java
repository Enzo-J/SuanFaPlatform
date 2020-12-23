package com.zkwg.modelmanager.utils;

import com.zkwg.modelmanager.ModelManagerApplication;
import com.zkwg.modelmanager.mapper.ModelMapper;
import io.minio.PutObjectOptions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelManagerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class K8sUtilsTest {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public K8sProperties k8sProperties;

    @Test
    public void k8sUtilsTest(){

        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));
        System.out.println(dateTime);
//        new K8sClientUtils(k8sProperties);
    }

    public static void main(String[] args) {
//        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));
//        System.out.println(dateTime);

        String url = "s3://SKLEARN-SERVER/2020-06-30-17-03-53";

        Pattern pattern = Pattern.compile("^s3://(\\W*|-*)/(_*)");
        Matcher m =  pattern.matcher(url);

        if (m.find( )) {
            System.out.println("Found value: " + m.group(0) );
            System.out.println("Found value: " + m.group(1) );
            System.out.println("Found value: " + m.group(2) );

            String bucketName = "";
        }

    }

}