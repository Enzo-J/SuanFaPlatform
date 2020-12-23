package com.zkwg.modelmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.io.Serializable;
import java.util.Date;
//import javax.persistence.*;

public class Container implements Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

//    @Column(name = "user_id")
    private Integer tenantId;

    /**
     * 容器名称
     */
    private String name;

    /**
     * 容器英文名称
     */
//    @Column(name = "name_en")
    private String nameEn;

    /**
     * 状态  1.未分配 2.已分配 3.运行中 4.超时 5.失败
     */
    private Byte status;

    /**
     * 所在命名空间
     */
    private String namespace;

    /**
     * 所在pod
     */
//    @Column(name = "pod_name")
    private String podName;

    /**
     * 创建时间
     */
//    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
//    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 创建人
     */
    private Integer creator;

    /**
     * 容器JSON定义
     */
//    @Column(name = "container_json")
    private String containerJson;

    /**
     * 运行状态JSON
     */
//    @Column(name = "run_json")
    private String runJson;

    /**
     * 是否删除
     */
//    @Column(name = "is_delete")
    private Byte isDelete;

    @TableField(exist = false)
    private String cpu;

    @TableField(exist = false)
    private String memory;

    private static final long serialVersionUID = 1L;

    public Container() {
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Container(String nameEn) {
        this.nameEn = nameEn;
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



    /**
     * 获取容器名称
     *
     * @return name - 容器名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置容器名称
     *
     * @param name 容器名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取容器英文名称
     *
     * @return name_en - 容器英文名称
     */
    public String getNameEn() {
        return nameEn;
    }

    /**
     * 设置容器英文名称
     *
     * @param nameEn 容器英文名称
     */
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    /**
     * 获取状态  1.未分配 2.已分配 3.运行中 4.超时 5.失败
     *
     * @return status - 状态  1.未分配 2.已分配 3.运行中 4.超时 5.失败
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态  1.未分配 2.已分配 3.运行中 4.超时 5.失败
     *
     * @param status 状态  1.未分配 2.已分配 3.运行中 4.超时 5.失败
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取所在命名空间
     *
     * @return namespace - 所在命名空间
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * 设置所在命名空间
     *
     * @param namespace 所在命名空间
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * 获取所在pod
     *
     * @return pod_name - 所在pod
     */
    public String getPodName() {
        return podName;
    }

    /**
     * 设置所在pod
     *
     * @param podName 所在pod
     */
    public void setPodName(String podName) {
        this.podName = podName;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return update_time - 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    /**
     * 获取容器JSON定义
     *
     * @return container_json - 容器JSON定义
     */
    public String getContainerJson() {
        return containerJson;
    }

    /**
     * 设置容器JSON定义
     *
     * @param containerJson 容器JSON定义
     */
    public void setContainerJson(String containerJson) {
        this.containerJson = containerJson;
    }

    /**
     * 获取运行状态JSON
     *
     * @return run_json - 运行状态JSON
     */
    public String getRunJson() {
        return runJson;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 设置运行状态JSON
     *
     * @param runJson 运行状态JSON
     */
    public void setRunJson(String runJson) {
        this.runJson = runJson;
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
        sb.append(", status=").append(status);
        sb.append(", namespace=").append(namespace);
        sb.append(", podName=").append(podName);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", creator=").append(creator);
        sb.append(", containerJson=").append(containerJson);
        sb.append(", runJson=").append(runJson);
        sb.append("]");
        return sb.toString();
    }
}