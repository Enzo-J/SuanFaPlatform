package com.zkwg.modelmanager.entity;

public class TaxpayerAddressSingle {

    private String  taxpayerCode; //纳税人识别号

    private String address; //企业注册地址

    private String type;//

    public TaxpayerAddressSingle() {
    }

    public String getTaxpayerCode() {
        return taxpayerCode;
    }

    public void setTaxpayerCode(String taxpayerCode) {
        this.taxpayerCode = taxpayerCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TaxpayerAddress{" +
                "taxpayerCode='" + taxpayerCode + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
