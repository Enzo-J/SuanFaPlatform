package com.zkwg.modelmanager.entity;

public class TaxpayerAddress {

    private String  taxpayerCode; //纳税人识别号

    private String address; //企业注册地址

    public TaxpayerAddress() {
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

    @Override
    public String toString() {
        return "TaxpayerAddress{" +
                "taxpayerCode='" + taxpayerCode + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
