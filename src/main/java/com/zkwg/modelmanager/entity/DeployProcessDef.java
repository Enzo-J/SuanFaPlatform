package com.zkwg.modelmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;
//import javax.persistence.*;

//@Table(name = "deploy_process_def")
public class DeployProcessDef implements Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

//    @Column(name = "user_id")
    private Integer tenantId;

    /**
     * 创建人
     */
    private Integer creator;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 流程是否可用 0:不可用 1：可用
     */
    private Byte flag;

    /**
     * 流程部署JSON—校验字符串
     */
//    @Column(name = "deploy_json_md5")
    private String deployJsonMd5;

//    @Column(name = "createTime")
    private Date createtime;

//    @Column(name = "updateTime")
    private Date updatetime;

    /**
     * 流程部署JSON
     */
//    @Column(name = "deploy_json")
    private String deployJson;

//    @Column(name = "models")
    private String models;

    /**
     * 1:
     */
    private String remark;

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

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * 获取流程名称
     *
     * @return name - 流程名称
     */
    public String getName() {
        return name;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    /**
     * 设置流程名称
     *
     * @param name 流程名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取流程是否可用 0:不可用 1：可用
     *
     * @return flag - 流程是否可用 0:不可用 1：可用
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置流程是否可用 0:不可用 1：可用
     *
     * @param flag 流程是否可用 0:不可用 1：可用
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }

    /**
     * 获取流程部署JSON—校验字符串
     *
     * @return deploy_json_md5 - 流程部署JSON—校验字符串
     */
    public String getDeployJsonMd5() {
        return deployJsonMd5;
    }

    /**
     * 设置流程部署JSON—校验字符串
     *
     * @param deployJsonMd5 流程部署JSON—校验字符串
     */
    public void setDeployJsonMd5(String deployJsonMd5) {
        this.deployJsonMd5 = deployJsonMd5;
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
     * @return updateTime
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * @param updatetime
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 获取流程部署JSON
     *
     * @return deploy_json - 流程部署JSON
     */
    public String getDeployJson() {
        return deployJson;
    }

    /**
     * 设置流程部署JSON
     *
     * @param deployJson 流程部署JSON
     */
    public void setDeployJson(String deployJson) {
        this.deployJson = deployJson;
    }

    public String getModels() {
        return models;
    }

    public void setModels(String models) {
        this.models = models;
    }

    /**
     * 获取1:
     *
     * @return remark - 1:
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置1:
     *
     * @param remark 1:
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
        sb.append(", name=").append(name);
        sb.append(", flag=").append(flag);
        sb.append(", deployJsonMd5=").append(deployJsonMd5);
        sb.append(", createtime=").append(createtime);
        sb.append(", updatetime=").append(updatetime);
        sb.append(", deployJson=").append(deployJson);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}