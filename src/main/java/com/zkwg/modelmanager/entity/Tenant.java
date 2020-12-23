package com.zkwg.modelmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;
//import javax.persistence.*;

public class Tenant implements Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 租户关联账户
     */
    private String account;

    /**
     * 租户代码
     */
//    @Column(name = "tenant_code")
    private String tenantCode;

    /**
     * 租户名称
     */
//    @Column(name = "tenant_name")
    private String tenantName;

    /**
     * 租户限定人数
     */
//    @Column(name = "user_limits")
    private Integer userLimits;

    /**
     * 授权开始时间
     */
//    @Column(name = "start_time")
    private Date startTime;

    /**
     * 授权结束时间
     */
//    @Column(name = "end_time")
    private Date endTime;

    /**
     * 状态 1:待初始化  2.正常  3.禁用  4.已删除
     */
    private Byte status;

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

//    @Column(name = "is_delete")
    private Byte isDelete;

    /**
     * 备注
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

    /**
     * 获取租户关联账户
     *
     * @return account - 租户关联账户
     */
    public String getAccount() {
        return account;
    }

    /**
     * 设置租户关联账户
     *
     * @param account 租户关联账户
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 获取租户代码
     *
     * @return tenant_code - 租户代码
     */
    public String getTenantCode() {
        return tenantCode;
    }

    /**
     * 设置租户代码
     *
     * @param tenantCode 租户代码
     */
    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    /**
     * 获取租户名称
     *
     * @return tenant_name - 租户名称
     */
    public String getTenantName() {
        return tenantName;
    }

    /**
     * 设置租户名称
     *
     * @param tenantName 租户名称
     */
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    /**
     * 获取租户限定人数
     *
     * @return user_limits - 租户限定人数
     */
    public Integer getUserLimits() {
        return userLimits;
    }

    /**
     * 设置租户限定人数
     *
     * @param userLimits 租户限定人数
     */
    public void setUserLimits(Integer userLimits) {
        this.userLimits = userLimits;
    }

    /**
     * 获取授权开始时间
     *
     * @return start_time - 授权开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置授权开始时间
     *
     * @param startTime 授权开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取授权结束时间
     *
     * @return end_time - 授权结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置授权结束时间
     *
     * @param endTime 授权结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取状态 1:待初始化  2.正常  3.禁用  4.已删除
     *
     * @return status - 状态 1:待初始化  2.正常  3.禁用  4.已删除
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态 1:待初始化  2.正常  3.禁用  4.已删除
     *
     * @param status 状态 1:待初始化  2.正常  3.禁用  4.已删除
     */
    public void setStatus(Byte status) {
        this.status = status;
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

    /**
     * @return is_delete
     */
    public Byte getIsDelete() {
        return isDelete;
    }

    /**
     * @param isDelete
     */
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
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
        sb.append(", account=").append(account);
        sb.append(", tenantCode=").append(tenantCode);
        sb.append(", tenantName=").append(tenantName);
        sb.append(", userLimits=").append(userLimits);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}