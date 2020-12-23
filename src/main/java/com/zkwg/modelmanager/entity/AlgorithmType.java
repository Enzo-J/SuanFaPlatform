package com.zkwg.modelmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

//@Table(name = "algorithm_type")
public class AlgorithmType implements Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer tenantId;
    /**
     * 一级分类
     */
//    @Column(name = "first_class_name")
    private String firstClassName;

    @TableField(value = "first_class_name_en")
    private String firstClassNameEN;

    /**
     * 二级分类
     */
//    @Column(name = "second_class_name")
    private String secondClassName;

    @TableField(value = "second_class_name_en")
    private String secondClassNameEN;

    /**
     * 默认分组id
     */
//    @Column(name = "default_group_id")
    private Integer defaultGroupId;

    /**
     * 概述
     */
    private String summarize;

    /**
     * 参数说明
     */
    private String paramsDesc;

    /**
     * 数据格式
     */
    private String dataFormat;

    /**
     * 原理简介
     */
    private String principle;


    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取一级分类
     *
     * @return first_class_name - 一级分类
     */
    public String getFirstClassName() {
        return firstClassName;
    }

    /**
     * 设置一级分类
     *
     * @param firstClassName 一级分类
     */
    public void setFirstClassName(String firstClassName) {
        this.firstClassName = firstClassName;
    }

    public String getFirstClassNameEN() {
        return firstClassNameEN;
    }

    public void setFirstClassNameEN(String firstClassNameEN) {
        this.firstClassNameEN = firstClassNameEN;
    }

    /**
     * 获取二级分类
     *
     * @return second_class_name - 二级分类
     */
    public String getSecondClassName() {
        return secondClassName;
    }

    /**
     * 设置二级分类
     *
     * @param secondClassName 二级分类
     */
    public void setSecondClassName(String secondClassName) {
        this.secondClassName = secondClassName;
    }

    public String getSecondClassNameEN() {
        return secondClassNameEN;
    }

    public void setSecondClassNameEN(String secondClassNameEN) {
        this.secondClassNameEN = secondClassNameEN;
    }

    /**
     * 获取默认分组id
     *
     * @return default_group_id - 默认分组id
     */
    public Integer getDefaultGroupId() {
        return defaultGroupId;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * 设置默认分组id
     *
     * @param defaultGroupId 默认分组id
     */
    public void setDefaultGroupId(Integer defaultGroupId) {
        this.defaultGroupId = defaultGroupId;
    }

    public String getSummarize() {
        return summarize;
    }

    public void setSummarize(String summarize) {
        this.summarize = summarize;
    }

    public String getParamsDesc() {
        return paramsDesc;
    }

    public void setParamsDesc(String paramsDesc) {
        this.paramsDesc = paramsDesc;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public String getPrinciple() {
        return principle;
    }

    public void setPrinciple(String principle) {
        this.principle = principle;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", firstClassName=").append(firstClassName);
        sb.append(", firstClassNameEN=").append(firstClassNameEN);
        sb.append(", secondClassName=").append(secondClassName);
        sb.append(", secondClassNameEN=").append(secondClassNameEN);
        sb.append(", defaultGroupId=").append(defaultGroupId);
        sb.append("]");
        return sb.toString();
    }
}