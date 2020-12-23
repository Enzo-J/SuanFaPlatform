package com.zkwg.modelmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;
//import javax.persistence.*;

//@Table(name = "resource_pool")
public class ResourcePool implements Serializable {
//    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

//    @Column(name = "user_id")
    private Integer userId;

    /**
     * 资源池名称
     */
    private String name;

    /**
     * 容器数量
     */
//    @Column(name = "container_num")
    private Integer containerNum;

    /**
     * 创建时间
     */
//    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 状态
     */
    private Integer status;

//    @Column(name = "pod_json")
    private String podJson;

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
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取资源池名称
     *
     * @return name - 资源池名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置资源池名称
     *
     * @param name 资源池名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取容器数量
     *
     * @return container_num - 容器数量
     */
    public Integer getContainerNum() {
        return containerNum;
    }

    /**
     * 设置容器数量
     *
     * @param containerNum 容器数量
     */
    public void setContainerNum(Integer containerNum) {
        this.containerNum = containerNum;
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
     * 获取创建人
     *
     * @return creator - 创建人
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 获取状态
     *
     * @return status - 状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", name=").append(name);
        sb.append(", containerNum=").append(containerNum);
        sb.append(", createTime=").append(createTime);
        sb.append(", creator=").append(creator);
        sb.append(", status=").append(status);
        sb.append(", podJson=").append(podJson);
        sb.append("]");
        return sb.toString();
    }
}