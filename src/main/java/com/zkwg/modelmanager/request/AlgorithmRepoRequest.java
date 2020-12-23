package com.zkwg.modelmanager.request;


import com.baomidou.mybatisplus.annotation.TableField;

public class AlgorithmRepoRequest extends PageInfoRequest {

    private String firstClassName;

    private String firstClassNameEN;

    private String secondClassName;

    private String secondClassNameEN;

    private String parameterName;

    public AlgorithmRepoRequest() {
    }

    public String getFirstClassName() {
        return firstClassName;
    }

    public void setFirstClassName(String firstClassName) {
        this.firstClassName = firstClassName;
    }

    public String getFirstClassNameEN() {
        return firstClassNameEN;
    }

    public void setFirstClassNameEN(String firstClassNameEN) {
        this.firstClassNameEN = firstClassNameEN;
    }

    public String getSecondClassName() {
        return secondClassName;
    }

    public void setSecondClassName(String secondClassName) {
        this.secondClassName = secondClassName;
    }

    public String getSecondClassNameEN() {
        return secondClassNameEN;
    }

    public void setSecondClassNameEN(String secondClassNameEN) {
        this.secondClassNameEN = secondClassNameEN;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
}
