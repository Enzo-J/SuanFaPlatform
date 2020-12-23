package com.zkwg.modelmanager.entity;

public class TaxpayerBuildingResult {

    private String  taxpayerCode; //纳税人识别号

    private String building; //所属楼宇（产业园）

    private String industrial_park;

    public TaxpayerBuildingResult() {
    }

    public String getTaxpayerCode() {
        return taxpayerCode;
    }

    public void setTaxpayerCode(String taxpayerCode) {
        this.taxpayerCode = taxpayerCode;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getIndustrial_park() {
        return industrial_park;
    }

    public void setIndustrial_park(String industrial_park) {
        this.industrial_park = industrial_park;
    }
}
