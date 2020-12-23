package com.zkwg.modelmanager.entity;

import java.util.List;
import java.util.Map;

public class BaseModelParam {

    private String sheetName="";

    private String sheetHeader="";

    private int totalRecord;

    private List<Map<String,Object>> sheetData;

    public BaseModelParam() {
    }

    public BaseModelParam(List<Map<String,Object>> sheetData) {
        this.sheetData = sheetData;
    }

    public BaseModelParam(List<Map<String,Object>> sheetData,int totalRecord) {
        this.sheetData = sheetData;
        this.totalRecord = totalRecord;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getSheetHeader() {
        return sheetHeader;
    }

    public void setSheetHeader(String sheetHeader) {
        this.sheetHeader = sheetHeader;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public List<Map<String, Object>> getSheetData() {
        return sheetData;
    }

    public void setSheetData(List<Map<String, Object>> sheetData) {
        this.sheetData = sheetData;
    }

    @Override
    public String toString() {
        return "BaseModelParam{" +
                "sheetName='" + sheetName + '\'' +
                ", sheetHeader='" + sheetHeader + '\'' +
                ", totalRecord=" + totalRecord +
                ", sheetData=" + sheetData +
                '}';
    }
}
