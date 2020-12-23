package com.zkwg.modelmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;
//import javax.persistence.*;

public class Model implements Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer tenantId;

    /**
     * 模型名称
     */
    private String name;

//    @Column(name = "name_en")
    private String nameEn;

    /**
     * 业务类型: 1：税收经济 2：税务稽查 3:风险管控 4:税收征管  5:企业服务  10：自然语言处理  11：舆情分析  12：知识图谱  13：语音技术  14：图像技术  15：视频技术  16：文字识别  17：人脸与人体识别
     */
    private Byte type;

    private Integer businessType;

    /**
     * 模型框架类型
     */
    private String implementation;

    /**
     * 镜像地址
     */
    private String image;

    /**
     * minio地址
     */
//    @Column(name = "minio_url")
    private String minioUrl;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 版本
     */
    private String version;

    /**
     * 图片
     */
    private String picture;

    /**
     * 创建人
     */
    private Integer creator;

    /**
     * 创建时间
     */
//    @Column(name = "createTime")
    private Date createtime;

    /**
     * 修改时间
     */
//    @Column(name = "updateTime")
    private Date updatetime;

    /**
     * 状态  -1：就绪（在打镜像或上传镜像） 1：创建  2：部署  3：部署失败 4：超时  5： 创建失败
     */
    private Byte status;

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

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否删除
     */
//    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 是否发布
     */
//    @Column(name = "is_publish")
    private Byte isPublish;

    /**
     * api文档
     */
//    @Column(name = "api_doc")
    private String apiDoc;

    @TableField(exist = false)
    private Integer isSub;

    // 模型保存路径
    @TableField(exist = false)
    private String path;

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
     * 获取模型名称
     *
     * @return name - 模型名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置模型名称
     *
     * @param name 模型名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return name_en
     */
    public String getNameEn() {
        return nameEn;
    }

    /**
     * @param nameEn
     */
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    /**
     * 获取业务类型: 1：税收经济 2：税务稽查 3:风险管控 4:税收征管  5:企业服务  10：自然语言处理  11：舆情分析  12：知识图谱  13：语音技术  14：图像技术  15：视频技术  16：文字识别  17：人脸与人体识别
     *
     * @return type - 业务类型: 1：税收经济 2：税务稽查 3:风险管控 4:税收征管  5:企业服务  10：自然语言处理  11：舆情分析  12：知识图谱  13：语音技术  14：图像技术  15：视频技术  16：文字识别  17：人脸与人体识别
     */
    public Byte getType() {
        return type;
    }

    /**
     * 设置业务类型: 1：税收经济 2：税务稽查 3:风险管控 4:税收征管  5:企业服务  10：自然语言处理  11：舆情分析  12：知识图谱  13：语音技术  14：图像技术  15：视频技术  16：文字识别  17：人脸与人体识别
     *
     * @param type 业务类型: 1：税收经济 2：税务稽查 3:风险管控 4:税收征管  5:企业服务  10：自然语言处理  11：舆情分析  12：知识图谱  13：语音技术  14：图像技术  15：视频技术  16：文字识别  17：人脸与人体识别
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * 获取模型框架类型
     *
     * @return implementation - 模型框架类型
     */
    public String getImplementation() {
        return implementation;
    }

    /**
     * 设置模型框架类型
     *
     * @param implementation 模型框架类型
     */
    public void setImplementation(String implementation) {
        this.implementation = implementation;
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

    /**
     * 获取镜像地址
     *
     * @return image - 镜像地址
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置镜像地址
     *
     * @param image 镜像地址
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * 获取minio地址
     *
     * @return minio_url - minio地址
     */
    public String getMinioUrl() {
        return minioUrl;
    }

    /**
     * 设置minio地址
     *
     * @param minioUrl minio地址
     */
    public void setMinioUrl(String minioUrl) {
        this.minioUrl = minioUrl;
    }

    /**
     * 获取文件名称
     *
     * @return filename - 文件名称
     */
    public String getFilename() {
        return filename;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * 设置文件名称
     *
     * @param filename 文件名称
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * 获取版本
     *
     * @return version - 版本
     */
    public String getVersion() {
        return version;
    }

    /**
     * 设置版本
     *
     * @param version 版本
     */
    public void setVersion(String version) {
        this.version = version;
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
     * 获取创建时间
     *
     * @return createTime - 创建时间
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * 设置创建时间
     *
     * @param createtime 创建时间
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * 获取修改时间
     *
     * @return updateTime - 修改时间
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * 设置修改时间
     *
     * @param updatetime 修改时间
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 获取状态  -1：就绪（在打镜像或上传镜像） 1：创建  2：部署  3：部署失败 4：超时  5： 创建失败
     *
     * @return status - 状态  -1：就绪（在打镜像或上传镜像） 1：创建  2：部署  3：部署失败 4：超时  5： 创建失败
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态  -1：就绪（在打镜像或上传镜像） 1：创建  2：部署  3：部署失败 4：超时  5： 创建失败
     *
     * @param status 状态  -1：就绪（在打镜像或上传镜像） 1：创建  2：部署  3：部署失败 4：超时  5： 创建失败
     */
    public void setStatus(Byte status) {
        this.status = status;
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

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public Integer getIsSub() {
        return isSub;
    }

    public void setIsSub(Integer isSub) {
        this.isSub = isSub;
    }

    /**
     * 获取是否发布
     *
     * @return is_publish - 是否发布
     */
    public Byte getIsPublish() {
        return isPublish;
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

    /**
     * 设置是否发布
     *
     * @param isPublish 是否发布
     */
    public void setIsPublish(Byte isPublish) {
        this.isPublish = isPublish;
    }

    /**
     * 获取api文档
     *
     * @return api_doc - api文档
     */
    public String getApiDoc() {
        return apiDoc;
    }

    /**
     * 设置api文档
     *
     * @param apiDoc api文档
     */
    public void setApiDoc(String apiDoc) {
        this.apiDoc = apiDoc;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", nameEn=").append(nameEn);
        sb.append(", type=").append(type);
        sb.append(", implementation=").append(implementation);
        sb.append(", image=").append(image);
        sb.append(", minioUrl=").append(minioUrl);
        sb.append(", filename=").append(filename);
        sb.append(", version=").append(version);
        sb.append(", picture=").append(picture);
        sb.append(", creator=").append(creator);
        sb.append(", createtime=").append(createtime);
        sb.append(", updatetime=").append(updatetime);
        sb.append(", status=").append(status);
        sb.append(", remark=").append(remark);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", isPublish=").append(isPublish);
        sb.append(", apiDoc=").append(apiDoc);
        sb.append("]");
        return sb.toString();
    }
}