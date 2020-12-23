package com.zkwg.modelmanager.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.zkwg.modelmanager.controller.ExcelManagerController;
import com.zkwg.modelmanager.entity.*;
import com.zkwg.modelmanager.service.ExecuteService;
import com.zkwg.modelmanager.utils.Result;
import com.zkwg.modelmanager.utils.ResultUtil;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelServiceImpl implements ExecuteService {

    private static Logger logger = LoggerFactory.getLogger(ExcelServiceImpl.class);

//    @Value("${model.service.address_url}")
//    private String addressUrl;
//
//    @Value("${model.service.pur_sel_url}")
//    private String purSelUrl;

    @Autowired
    private RestTemplate restTemplate;

//    public String get(Integer id){
//        return restTemplate.getForObject("http://localhost:8080/user?userId=id",String.class);
//    }

//    @GetMapping("getForEntity/{id}")
//    public User getById(@PathVariable(name = "id") String id) {
//        ResponseEntity<User> response = restTemplate.getForEntity("http://localhost/get/{id}", User.class, id);
//        User user = response.getBody();
//        return user;
//    }

//    @RequestMapping("saveUser")
//    public String save(User user) {
//        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost/save", user, String.class);
//        String body = response.getBody();
//        return body;
//    }


    @Override
    public <T> Result executeModel(BaseModelParam baseModelParam, String url, TypeReference<T> tf) {

        logger.info(baseModelParam.toString());

        // 调用模型进行匹配
        ResponseEntity<String> response = restTemplate.postForEntity(url, baseModelParam, String.class);
        String body = response.getBody();

        logger.info(url + " 匹配返回值： "+body);

        T t = JSON.parseObject(body, tf);

        return ResultUtil.success(t);

    }

//    @Override
//    public Result executeTtaxpayerAddress(BaseModelParam baseModelParam) {
//
//        logger.info(baseModelParam.toString());
//
//        ResponseEntity<String> response = restTemplate.postForEntity(addressUrl, baseModelParam, String.class);
//        String body = response.getBody();
//
//        logger.info(addressUrl + " 地址匹配返回值： "+body);
//
//        List<TaxpayerBuildingResult> list = JSON.parseObject(body, new TypeReference<ArrayList<TaxpayerBuildingResult>>() {});
//
////        TaxpayerBuildingResult taxpayerBuildingResult = new TaxpayerBuildingResult();
////        taxpayerBuildingResult.setBuilding("1980文化创意园");
////        taxpayerBuildingResult.setTaxpayerCode("平安大厦");
////
////        List<TaxpayerBuildingResult> list = new ArrayList<>();
////        list.add(taxpayerBuildingResult);
//
//        return ResultUtil.success(list);
//
//    }

//    @Override
//    public Result executeSinglePurSelSto(BaseModelParam baseModelParam) {
//
//        logger.info(baseModelParam.toString());
//
//        ResponseEntity<String> response = restTemplate.postForEntity(purSelUrl, baseModelParam, String.class);
//        String body = response.getBody();
//
//        logger.info(purSelUrl+" 地址匹配返回值： "+body);
//
//        List<PurSelStoMatchResult> list = JSON.parseObject(body, new TypeReference<ArrayList<PurSelStoMatchResult>>() {});
//
//
////        PurSelStoMatchResult purSelStoMatchResult = new PurSelStoMatchResult();
////        purSelStoMatchResult.setTaxpayerCode(new String[]{"xxx","xx","xx"});
////        purSelStoMatchResult.setPurItemNameMatch("0.4");
////        purSelStoMatchResult.setPurMoneyMatch("0.67");
////        purSelStoMatchResult.setIsMatch("是");
//
////        List<PurSelStoMatchResult> list = new ArrayList<>();
////        list.add(purSelStoMatchResult);
//
//        return ResultUtil.success(list);
//
//    }
}
