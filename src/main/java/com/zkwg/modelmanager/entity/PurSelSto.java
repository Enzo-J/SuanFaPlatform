package com.zkwg.modelmanager.entity;
// purchase-sell-stock
public class PurSelSto {

    private String  taxpayerCode; //纳税人识别号

    private String purItemName; //进项货物品名

    private double purItemMoney; // 进项货物金额

    private String sellItemName; // 销项货物品名

    private double sellItemMoney;// 销项货物金额

    public PurSelSto() {
    }

    public String getTaxpayerCode() {
        return taxpayerCode;
    }

    public void setTaxpayerCode(String taxpayerCode) {
        this.taxpayerCode = taxpayerCode;
    }

    public String getPurItemName() {
        return purItemName;
    }

    public void setPurItemName(String purItemName) {
        this.purItemName = purItemName;
    }

    public double getPurItemMoney() {
        return purItemMoney;
    }

    public void setPurItemMoney(double purItemMoney) {
        this.purItemMoney = purItemMoney;
    }

    public String getSellItemName() {
        return sellItemName;
    }

    public void setSellItemName(String sellItemName) {
        this.sellItemName = sellItemName;
    }

    public double getSellItemMoney() {
        return sellItemMoney;
    }

    public void setSellItemMoney(double sellItemMoney) {
        this.sellItemMoney = sellItemMoney;
    }

    @Override
    public String toString() {
        return "PurSelSto{" +
                "taxpayerCode='" + taxpayerCode + '\'' +
                ", purItemName='" + purItemName + '\'' +
                ", purItemMoney='" + purItemMoney + '\'' +
                ", sellItemName='" + sellItemName + '\'' +
                ", sellItemMoney='" + sellItemMoney + '\'' +
                '}';
    }
}
