package com.zkwg.modelmanager.entity;
// purchase-sell-stock-param

//{
//        sheetName:"数据导入";
//        sheetHeader:"纳税人识别号、企业进项品名、企业进项金额、企业销项品名、企业销项金额";
//        totalRecord:"1";
//        sheetData:[
//        {
//        "纳税人识别号":XXXXX;
//        "企业进项品名":xxxxxx;
//        "企业进项金额”：XXX;
//        "企业销项品名”：XXX;
//        "企业销项金额”：XXX;
//        },
//        {
//        "纳税人识别号":XXXXX;
//        "企业进项品名":xxxxxx;
//        "企业进项金额”：XXX;
//        "企业销项品名”：XXX;
//        "企业销项金额”：XXX;
//        }
//        }
//        ]

import java.util.List;
import java.util.Map;

public class PurSelStoParam extends BaseModelParam {

    private List<Map<String,Object>> sheetData;

    public PurSelStoParam() {
    }

    public PurSelStoParam(List<Map<String,Object>> sheetData) {
        this.sheetData = sheetData;
    }

    public List<Map<String, Object>> getSheetData() {
        return sheetData;
    }

    public void setSheetData(List<Map<String, Object>> sheetData) {
        this.sheetData = sheetData;
    }

    @Override
    public String toString() {
        return "PurSelStoParam{" +
                "sheetData=" + sheetData +
                '}';
    }
}
