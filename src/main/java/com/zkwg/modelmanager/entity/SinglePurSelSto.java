package com.zkwg.modelmanager.entity;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public class SinglePurSelSto {

    List<String> purItemNameArr;

    List<Double> purItemMoneyArr;

    List<String> sellItemNameArr;

    List<Double> sellItemMoneyArr;

    public SinglePurSelSto() {
    }

    public List<String> getPurItemNameArr() {
        return purItemNameArr;
    }

    public void setPurItemNameArr(List<String> purItemNameArr) {
        this.purItemNameArr = purItemNameArr;
    }

    public List<Double> getPurItemMoneyArr() {
        return purItemMoneyArr;
    }

    public void setPurItemMoneyArr(List<Double> purItemMoneyArr) {
        this.purItemMoneyArr = purItemMoneyArr;
    }

    public List<String> getSellItemNameArr() {
        return sellItemNameArr;
    }

    public void setSellItemNameArr(List<String> sellItemNameArr) {
        this.sellItemNameArr = sellItemNameArr;
    }

    public List<Double> getSellItemMoneyArr() {
        return sellItemMoneyArr;
    }

    public void setSellItemMoneyArr(List<Double> sellItemMoneyArr) {
        this.sellItemMoneyArr = sellItemMoneyArr;
    }

    @Override
    public String toString() {
        return "SinglePurSelSto{" +
                "purItemNameArr=" + purItemNameArr +
                ", purItemMoneyArr=" + purItemMoneyArr +
                ", sellItemNameArr=" + sellItemNameArr +
                ", sellItemMoneyArr=" + sellItemMoneyArr +
                '}';
    }
}
