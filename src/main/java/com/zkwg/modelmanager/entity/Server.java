package com.zkwg.modelmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;
//import javax.persistence.*;

public class Server implements Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

//    @Column(name = "user_id")
    private Integer tenantId;

    private Integer creator;

    /**
     * 流程定义id
     */
//    @Column(name = "process_def_id")
    private Integer processDefId;

    /**
     * 服务实例名称
     */
    private String name;

    /**
     * 服务实例状态 1:正在运行 2：准备停止 3：停止  4.运行失败  5：发布 6：超时  7.创建  8.部署中
     */
    private Byte status;

    /**
     * 部署json的MD5
     */
//    @Column(name = "server_json_md5")
    private String serverJsonMd5;

    /**
     * 包含的容器 id-id-id...
     */
    private String containers;

    private String models;

    /**
     * 图片
     */
    private String picture;

    private Integer businessType;

    /**
     * 服务类型
     */
    private Integer type;

    private String implementation;

//    @Column(name = "createTime")
    private Date createtime;

    /**
     * 是否发布
     */
//    @Column(name = "is_publish")
    private Byte isPublish;

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    /**
     * 是否删除
     */
//    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 服务JSON定义
     */
//    @Column(name = "server_json")
    private String serverJson;

    /**
     * 服务实例运行结果JSON
     */
//    @Column(name = "run_result_json")
    private String runResultJson;

//    @Column(name = "pod_json")
    private String podJson;

    public String getImplementation() {
        return implementation;
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

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

    private Integer subscribeNum;

    private Integer viewNum;

    private Integer callNum;

    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private Integer isSub;

    private static final long serialVersionUID = 1L;

    public Server() {

    }

    public Server(String serverJsonMd5) {
        this.serverJsonMd5 = serverJsonMd5;
    }

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

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * 获取流程定义id
     *
     * @return process_def_id - 流程定义id
     */
    public Integer getProcessDefId() {
        return processDefId;
    }

    /**
     * 设置流程定义id
     *
     * @param processDefId 流程定义id
     */
    public void setProcessDefId(Integer processDefId) {
        this.processDefId = processDefId;
    }

    /**
     * 获取服务实例名称
     *
     * @return name - 服务实例名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置服务实例名称
     *
     * @param name 服务实例名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取服务实例状态 1:正在运行 2：准备停止 3：停止  4.运行失败  5：发布 6：超时  7.创建  8.部署中
     *
     * @return status - 服务实例状态 1:正在运行 2：准备停止 3：停止  4.运行失败  5：发布 6：超时  7.创建  8.部署中
     */
    public Byte getStatus() {
        return status;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    /**
     * 设置服务实例状态 1:正在运行 2：准备停止 3：停止  4.运行失败  5：发布 6：超时  7.创建  8.部署中
     *
     * @param status 服务实例状态 1:正在运行 2：准备停止 3：停止  4.运行失败  5：发布 6：超时  7.创建  8.部署中
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取部署json的MD5
     *
     * @return server_json_md5 - 部署json的MD5
     */
    public String getServerJsonMd5() {
        return serverJsonMd5;
    }

    /**
     * 设置部署json的MD5
     *
     * @param serverJsonMd5 部署json的MD5
     */
    public void setServerJsonMd5(String serverJsonMd5) {
        this.serverJsonMd5 = serverJsonMd5;
    }

    /**
     * 获取包含的容器 id-id-id...
     *
     * @return containers - 包含的容器 id-id-id...
     */
    public String getContainers() {
        return containers;
    }

    /**
     * 设置包含的容器 id-id-id...
     *
     * @param containers 包含的容器 id-id-id...
     */
    public void setContainers(String containers) {
        this.containers = containers;
    }

    public Integer getIsSub() {
        return isSub;
    }

    public void setIsSub(Integer isSub) {
        this.isSub = isSub;
    }

    /**
     * @return models
     */
    public String getModels() {
        return models;
    }

    /**
     * @param models
     */
    public void setModels(String models) {
        this.models = models;
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

    public Integer getSubscribeNum() {
        return subscribeNum;
    }

    public void setSubscribeNum(Integer subscribeNum) {
        this.subscribeNum = subscribeNum;
    }

    public Integer getViewNum() {
        return viewNum;
    }

    public void setViewNum(Integer viewNum) {
        this.viewNum = viewNum;
    }

    public Integer getCallNum() {
        return callNum;
    }

    public void setCallNum(Integer callNum) {
        this.callNum = callNum;
    }

    /**
     * 获取图片
     *
     * @return picture - 图片
     */
    public String getPicture() {
        return picture;
    }

    /**
     * 设置图片
     *
     * @param picture 图片
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * 获取服务类型
     *
     * @return type - 服务类型
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置服务类型
     *
     * @param type 服务类型
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return createTime
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * @param createtime
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取是否发布
     *
     * @return is_publish - 是否发布
     */
    public Byte getIsPublish() {
        return isPublish;
    }

    /**
     * 设置是否发布
     *
     * @param isPublish 是否发布
     */
    public void setIsPublish(Byte isPublish) {
        this.isPublish = isPublish;
    }

    /**
     * 获取是否删除
     *
     * @return is_delete - 是否删除
     */
    public Byte getIsDelete() {
        return isDelete;
    }

    /**
     * 设置是否删除
     *
     * @param isDelete 是否删除
     */
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 获取服务JSON定义
     *
     * @return server_json - 服务JSON定义
     */
    public String getServerJson() {
        return serverJson;
    }

    /**
     * 设置服务JSON定义
     *
     * @param serverJson 服务JSON定义
     */
    public void setServerJson(String serverJson) {
        this.serverJson = serverJson;
    }

    /**
     * 获取服务实例运行结果JSON
     *
     * @return run_result_json - 服务实例运行结果JSON
     */
    public String getRunResultJson() {
        return runResultJson;
    }

    /**
     * 设置服务实例运行结果JSON
     *
     * @param runResultJson 服务实例运行结果JSON
     */
    public void setRunResultJson(String runResultJson) {
        this.runResultJson = runResultJson;
    }

    /**
     * @return pod_json
     */
    public String getPodJson() {
        return podJson;
    }

    /**
     * @param podJson
     */
    public void setPodJson(String podJson) {
        this.podJson = podJson;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", processDefId=").append(processDefId);
        sb.append(", name=").append(name);
        sb.append(", status=").append(status);
        sb.append(", serverJsonMd5=").append(serverJsonMd5);
        sb.append(", containers=").append(containers);
        sb.append(", models=").append(models);
        sb.append(", picture=").append(picture);
        sb.append(", type=").append(type);
        sb.append(", createtime=").append(createtime);
        sb.append(", isPublish=").append(isPublish);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serverJson=").append(serverJson);
        sb.append(", runResultJson=").append(runResultJson);
        sb.append(", podJson=").append(podJson);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}