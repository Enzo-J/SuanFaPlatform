package com.zkwg.modelmanager.entity;


import java.util.List;
import java.util.Map;

public class TaxpayerAddressParam extends BaseModelParam {

    private List<Map<String,Object>> sheetData;

    public TaxpayerAddressParam() {
    }

    public TaxpayerAddressParam(List<Map<String,Object>> sheetData) {
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
        return "TaxpayerAddressParam{" +
                "sheetData=" + sheetData +
                '}';
    }
}
