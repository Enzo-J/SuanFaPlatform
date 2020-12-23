//package com.zkwg.modelmanager.utils;
//
//import org.mybatis.generator.api.MyBatisGenerator;
//import org.mybatis.generator.config.Configuration;
//import org.mybatis.generator.config.xml.ConfigurationParser;
//import org.mybatis.generator.internal.DefaultShellCallback;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//public class GeneratorMybatis {
//
//    public void generator() throws Exception{
//        List<String> warnings = new ArrayList<String>();
//        boolean overwrite = true;
//        //指定 逆向工程配置文件
//        File configFile = new File("E:\\项目\\github\\model-manager\\src\\main\\resources\\mybatis\\generatorConfig.xml");
//        ConfigurationParser cp = new ConfigurationParser(warnings);
//        Configuration config = cp.parseConfiguration(configFile);
//        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
//        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
//                callback, warnings);
//        myBatisGenerator.generate(null);
//    }
//
//    public static void main(String[] args) throws Exception {
//        try {
//            GeneratorMybatis generatorSqlmap = new GeneratorMybatis();
//            generatorSqlmap.generator();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}