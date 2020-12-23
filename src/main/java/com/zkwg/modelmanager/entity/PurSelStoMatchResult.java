package com.zkwg.modelmanager.entity;
// purchase-sell-stock
public class PurSelStoMatchResult {

    private String  taxpayerCode; //纳税人识别号

    private String isMatch; //是否匹配

    private String purItemNameMatch; // 进项品名匹配度

    private String purMoneyMatch; // 进项金额匹配度

    private String[] goods_unmatched;

    public PurSelStoMatchResult() {
    }

    public String getTaxpayerCode() {
        return taxpayerCode;
    }

    public void setTaxpayerCode(String taxpayerCode) {
        this.taxpayerCode = taxpayerCode;
    }

    public String getIsMatch() {
        return isMatch;
    }

    public void setIsMatch(String isMatch) {
        this.isMatch = isMatch;
    }

    public String getPurItemNameMatch() {
        return purItemNameMatch;
    }

    public void setPurItemNameMatch(String purItemNameMatch) {
        this.purItemNameMatch = purItemNameMatch;
    }

    public String getPurMoneyMatch() {
        return purMoneyMatch;
    }

    public void setPurMoneyMatch(String purMoneyMatch) {
        this.purMoneyMatch = purMoneyMatch;
    }

    public String[] getGoods_unmatched() {
        return goods_unmatched;
    }

    public void setGoods_unmatched(String[] goods_unmatched) {
        this.goods_unmatched = goods_unmatched;
    }
}
