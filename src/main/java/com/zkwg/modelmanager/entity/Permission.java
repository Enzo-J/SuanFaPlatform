package com.zkwg.modelmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
//import javax.persistence.*;

public class Permission implements Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 权限编码
     */
//    @Column(name = "perms_code")
    private String permsCode;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 类型，-1系统 0:目录 1：菜单，2：按钮，3：其他
     */
    private Byte type;

    /**
     * 父权限ID
     */
//    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 权限层级
     */
    private Byte level;

    /**
     * 请求url
     */
    private String path;

    /**
     * 菜单组件
     */
    private String component;

    /**
     * 图标
     */
    private String icon;

    /**
     * 权限在当前模块下的顺序，由小到大
     */
    private Byte sort;

    /**
     * 是否为外链
     */
//    @Column(name = "is_frame")
    private Byte isFrame;

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

    private Byte isDelete;

    /**
     * 备注
     */
    private String remark;


    @TableField(exist = false)
    private List<Permission> children;

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
     * 获取权限编码
     *
     * @return perms_code - 权限编码
     */
    public String getPermsCode() {
        return permsCode;
    }

    /**
     * 设置权限编码
     *
     * @param permsCode 权限编码
     */
    public void setPermsCode(String permsCode) {
        this.permsCode = permsCode;
    }

    /**
     * 获取权限名称
     *
     * @return name - 权限名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置权限名称
     *
     * @param name 权限名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取类型，-1系统 0:目录 1：菜单，2：按钮，3：其他
     *
     * @return type - 类型，-1系统 0:目录 1：菜单，2：按钮，3：其他
     */
    public Byte getType() {
        return type;
    }

    /**
     * 设置类型，-1系统 0:目录 1：菜单，2：按钮，3：其他
     *
     * @param type 类型，-1系统 0:目录 1：菜单，2：按钮，3：其他
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * 获取父权限ID
     *
     * @return parent_id - 父权限ID
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * 设置父权限ID
     *
     * @param parentId 父权限ID
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取权限层级
     *
     * @return level - 权限层级
     */
    public Byte getLevel() {
        return level;
    }

    /**
     * 设置权限层级
     *
     * @param level 权限层级
     */
    public void setLevel(Byte level) {
        this.level = level;
    }

    /**
     * 获取请求url
     *
     * @return path - 请求url
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置请求url
     *
     * @param path 请求url
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取菜单组件
     *
     * @return component - 菜单组件
     */
    public String getComponent() {
        return component;
    }

    /**
     * 设置菜单组件
     *
     * @param component 菜单组件
     */
    public void setComponent(String component) {
        this.component = component;
    }

    /**
     * 获取图标
     *
     * @return icon - 图标
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 设置图标
     *
     * @param icon 图标
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 获取权限在当前模块下的顺序，由小到大
     *
     * @return sort - 权限在当前模块下的顺序，由小到大
     */
    public Byte getSort() {
        return sort;
    }

    /**
     * 设置权限在当前模块下的顺序，由小到大
     *
     * @param sort 权限在当前模块下的顺序，由小到大
     */
    public void setSort(Byte sort) {
        this.sort = sort;
    }

    /**
     * 获取是否为外链
     *
     * @return is_frame - 是否为外链
     */
    public Byte getIsFrame() {
        return isFrame;
    }

    /**
     * 设置是否为外链
     *
     * @param isFrame 是否为外链
     */
    public void setIsFrame(Byte isFrame) {
        this.isFrame = isFrame;
    }

    /**
     * @return status
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * @param status
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

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Permission> getChildren() {
        return children;
    }

    public void setChildren(List<Permission> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", permsCode=").append(permsCode);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", parentId=").append(parentId);
        sb.append(", level=").append(level);
        sb.append(", path=").append(path);
        sb.append(", component=").append(component);
        sb.append(", icon=").append(icon);
        sb.append(", sort=").append(sort);
        sb.append(", isFrame=").append(isFrame);
        sb.append(", status=").append(status);
        sb.append(", creator=").append(creator);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}