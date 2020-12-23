package com.zkwg.modelmanager.controller;

import com.alibaba.fastjson.TypeReference;
import com.zkwg.modelmanager.entity.*;
import com.zkwg.modelmanager.service.ExecuteService;
import com.zkwg.modelmanager.utils.ExcelHead;
import com.zkwg.modelmanager.utils.ExcelUtils;
import com.zkwg.modelmanager.utils.Result;
import com.zkwg.modelmanager.utils.ResultUtil;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@RestController("/excel")
public class ExcelManagerController {

    private static Logger logger = LoggerFactory.getLogger(ExcelManagerController.class);

    @Value("${model.service.address_url}")
    private String addressUrl;

    @Value("${model.service.pur_sel_url}")
    private String purSelUrl;

    @Autowired
    private ExecuteService excelService;

    // 地址解析
    @PostMapping("/uploadTaxpayerAddress")
    public Result uploadTaxpayerAddress(@RequestParam("file") MultipartFile file,int type) {

        if (file.isEmpty()) {
            return ResultUtil.failure(null,"上传失败，请选择文件");
        }

        List<String[]> headList = new ArrayList<String[]>();
        headList.add(new String[]{"纳税人识别号","taxpayerCode"});
        headList.add(new String[]{"企业注册地址","address"});

        if(type == 1){ // 解析文件
            try {

                List<TaxpayerAddress> list = parseExcel2List(file,headList,TaxpayerAddress.class);

                return ResultUtil.success(list);

            } catch (Exception e){
                logger.error(" 解析Excel文件报错 ",e.toString());
                return ResultUtil.failure(null,e.toString());
            }
        }
        if(type == 2){
            // 解析
            BaseModelParam baseModelParam = null;
            try {
                baseModelParam = parseTaxpayerAddress(file,headList);
            } catch (Exception e) {
                logger.error(" 解析Excel文件报错 ",e.toString());
                return ResultUtil.failure(null,e.toString());
            }

            return excelService.executeModel(baseModelParam,addressUrl,new TypeReference<ArrayList<TaxpayerBuildingResult>>() {});
        }

        return ResultUtil.failure(null,"type 参数取1或2,1：解析Excel,2:匹配Excel内容");

    }

    // 单行地址解析
    @PostMapping("/singleTaxpayerAddress")
    public Result singlePurSelSto(@RequestBody TaxpayerAddressSingle taxpayerAddressSingle) {

        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("纳税人识别号",taxpayerAddressSingle.getTaxpayerCode());
        map.put("企业注册地址",taxpayerAddressSingle.getAddress());
        list.add(map);
        // 执行
        return excelService.executeModel(new BaseModelParam(list,1),addressUrl,new TypeReference<ArrayList<TaxpayerBuildingResult>>() {});
    }

    // 进销解析
    @PostMapping("/uploadPurSelSto")
    public Result uploadPurSelSto(@RequestParam("file") MultipartFile file,int type) {
        if (file.isEmpty()) {
            return ResultUtil.failure(null,"上传失败，请选择文件");
        }

        List<String[]> headList = new ArrayList<String[]>();
        headList.add(new String[]{"纳税人识别号","taxpayerCode"});
        headList.add(new String[]{"进项货物品名","purItemName"});
        headList.add(new String[]{"进项货物金额","purItemMoney"});
        headList.add(new String[]{"销项货物品名","sellItemName"});
        headList.add(new String[]{"销项货物金额","sellItemMoney"});

        if(type == 1) { // 解析文件
            try {

                List<PurSelSto> list = parseExcel2List(file,headList,PurSelSto.class);
                return ResultUtil.success(list);

            } catch (Exception e){
                logger.error(" 解析Excel文件报错 ",e.toString());
                return ResultUtil.failure(null,e.toString());
            }
        }

        if(type == 2){
            // 解析
            BaseModelParam baseModelParam = null;
            try {
                baseModelParam = parsePurSelSto(file,headList);
            } catch (Exception e) {
                logger.error(" 解析Excel文件报错 ",e.toString());
                return ResultUtil.failure(null,e.toString());
            }
            // 执行
            return excelService.executeModel(baseModelParam,purSelUrl,new TypeReference<ArrayList<PurSelStoMatchResult>>() {});
        }

        return ResultUtil.failure(null,"type 参数取1或2,1：解析Excel,2:匹配Excel内容");
    }


    // 单行进销解析
    @PostMapping("/singlePurSelSto")
    public Result singlePurSelSto(@RequestBody SinglePurSelSto purSelStoList) {

        logger.info(purSelStoList.toString());

        List<String> purItemNameArr = purSelStoList.getPurItemNameArr();
        List<Double> purItemMoneyArr = purSelStoList.getPurItemMoneyArr();
        List<String> sellItemNameArr = purSelStoList.getSellItemNameArr();
        List<Double> sellItemMoneyArr = purSelStoList.getSellItemMoneyArr();

        int purItemLength =  purItemNameArr.size();
        int sellItemLength =  sellItemNameArr.size();
        int len = purItemLength > sellItemLength ? purItemLength : sellItemLength;

        Long curentTime = System.currentTimeMillis();
        List<Map<String, Object>> sheetData = new ArrayList<>();
        for(int i=0; i<len; i++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("纳税人识别号", curentTime);
            map.put("进项货物品名",i < purItemLength ? purItemNameArr.get(i) : "");
            map.put("进项货物金额",i < purItemMoneyArr.size() ? purItemMoneyArr.get(i) : 1);
            map.put("销项货物品名",i < sellItemLength ? sellItemNameArr.get(i) : "");
            map.put("销项货物金额",i < sellItemMoneyArr.size() ? sellItemMoneyArr.get(i) : 1);
            sheetData.add(map);
        }

//        List<Map<String, Object>> sheetData = purSelStoList.stream().map( purSelSto -> {
//            Map<String,Object> map = new HashMap<String,Object>();
//            map.put("纳税人识别号",purSelSto.getTaxpayerCode());
//            map.put("进项货物品名",purSelSto.getPurItemName());
//            map.put("进项货物金额",purSelSto.getPurItemMoney());
//            map.put("销项货物品名",purSelSto.getSellItemName());
//            map.put("销项货物金额",purSelSto.getSellItemMoney());
//            return map;
//        } ).collect(Collectors.toList());

        // 执行
        return excelService.executeModel(new BaseModelParam(sheetData,sheetData.size()),purSelUrl,new TypeReference<ArrayList<PurSelStoMatchResult>>() {});
    }


    /**
     * 解析地址匹配文件
     */
    private BaseModelParam parseTaxpayerAddress(MultipartFile file,List<String[]> headList) throws Exception {


        List<TaxpayerAddress> list = parseExcel2List(file,headList,TaxpayerAddress.class);

        List<Map<String, Object>> sheetData = null;
        if(list != null && list.size() > 0){
            sheetData = list.stream().map( taxpayerAddress -> {
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("纳税人识别号",taxpayerAddress.getTaxpayerCode());
                map.put("企业注册地址",taxpayerAddress.getAddress());
                return map;
            } ).collect(Collectors.toList());

        }

        return new BaseModelParam(sheetData,sheetData.size());
    }

    /**
     * 解析进销匹配文件
     */
    private BaseModelParam parsePurSelSto(MultipartFile file,List<String[]> headList) throws Exception {

        List<PurSelSto> list = parseExcel2List(file,headList,PurSelSto.class);

        List<Map<String, Object>> sheetData = null;
        if(list != null && list.size() > 0){
            sheetData = list.stream().map( purSelSto -> {
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("纳税人识别号",purSelSto.getTaxpayerCode());
                map.put("进项货物品名",purSelSto.getPurItemName());
                map.put("进项货物金额",purSelSto.getPurItemMoney());
                map.put("销项货物品名",purSelSto.getSellItemName());
                map.put("销项货物金额",purSelSto.getSellItemMoney());
                return map;
            } ).collect(Collectors.toList());

        }

        return new BaseModelParam(sheetData,sheetData.size());
    }


    /**
     * 解析进销匹配文件
     */
    private <T> List<T> parseExcel2List(MultipartFile file,List<String[]> excelHeadMapList,Class returenClass) throws Exception {

        InputStream in = file.getInputStream();

        // 转换为List<ExcelHead>
        List<ExcelHead> excelHeads = excelHeadMapList.stream().map(m -> {

            int len = m.length;
            return len == 2 ? new ExcelHead(m[0], m[1]) :
                   len == 3 ? new ExcelHead(m[0], m[1], "true".equals(m[2]) ? true : false)
                            : new ExcelHead();

        }).collect(Collectors.toList());

        List<T> list = ExcelUtils.readExcelToEntity(returenClass, in, file.getOriginalFilename(), excelHeads);

        return list;
    }


}
