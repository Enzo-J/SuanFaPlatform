package com.zkwg.modelmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;
//import javax.persistence.*;

//@Table(name = "sub_org")
public class SubOrg implements Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 组织id
     */
//    @Column(name = "org_id")
    private Integer orgId;

    /**
     * 机构编码
     */
    private String code;

    /**
     * 父机构id
     */
//    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private Byte type;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 状态
     */
    private Byte status;

    /**
     * 创建人
     */
    private String creator;

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
     * 获取组织id
     *
     * @return org_id - 组织id
     */
    public Integer getOrgId() {
        return orgId;
    }

    /**
     * 设置组织id
     *
     * @param orgId 组织id
     */
    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    /**
     * 获取机构编码
     *
     * @return code - 机构编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置机构编码
     *
     * @param code 机构编码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取父机构id
     *
     * @return parent_id - 父机构id
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * 设置父机构id
     *
     * @param parentId 父机构id
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取类型
     *
     * @return type - 类型
     */
    public Byte getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type 类型
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * 获取负责人
     *
     * @return leader - 负责人
     */
    public String getLeader() {
        return leader;
    }

    /**
     * 设置负责人
     *
     * @param leader 负责人
     */
    public void setLeader(String leader) {
        this.leader = leader;
    }

    /**
     * 获取状态
     *
     * @return status - 状态
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(Byte status) {
        this.status = status;
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
        sb.append(", orgId=").append(orgId);
        sb.append(", code=").append(code);
        sb.append(", parentId=").append(parentId);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", leader=").append(leader);
        sb.append(", status=").append(status);
        sb.append(", creator=").append(creator);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}