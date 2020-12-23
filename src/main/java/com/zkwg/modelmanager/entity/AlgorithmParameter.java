package com.zkwg.modelmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

//@Table(name = "parameter")
@TableName(value = "parameter")
public class AlgorithmParameter implements Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer tenantId;

    /**
     * 参数组id
     */
//    @Column(name = "group_id")
    private Integer groupId;

    /**
     * 参数名称
     */
    private String name;

//    @Column(name = "param_name")
    private String paramName;

    /**
     * 参数说明
     */
    private String remark;

    /**
     * 参数类型   1.数值  2.布尔值 3.字符串枚举
     */
    private String type;

    /**
     * 创建时间
     */
//    @Column(name = "create_time")
    private Date createTime;

    /**
     * 参数值
     */
//    @Column(name = "value_scope")
    private String valueScope;

    /**
     * 参数默认值
     */
//    @Column(name = "default_value")
    private String defaultValue;

    private static final long serialVersionUID = 1L;

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
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
     * 获取参数组id
     *
     * @return group_id - 参数组id
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * 设置参数组id
     *
     * @param groupId 参数组id
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    /**
     * 获取参数名称
     *
     * @return name - 参数名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置参数名称
     *
     * @param name 参数名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return param_name
     */
    public String getParamName() {
        return paramName;
    }

    /**
     * @param paramName
     */
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    /**
     * 获取参数说明
     *
     * @return desc - 参数说明
     */
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取参数类型   1.数值  2.布尔值 3.字符串枚举
     *
     * @return type - 参数类型   1.数值  2.布尔值 3.字符串枚举
     */
    public String getType() {
        return type;
    }

    /**
     * 设置参数类型   1.数值  2.布尔值 3.字符串枚举
     *
     * @param type 参数类型   1.数值  2.布尔值 3.字符串枚举
     */
    public void setType(String type) {
        this.type = type;
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
     * 获取参数值
     *
     * @return value_scope - 参数值
     */
    public String getValueScope() {
        return valueScope;
    }

    /**
     * 设置参数值
     *
     * @param valueScope 参数值
     */
    public void setValueScope(String valueScope) {
        this.valueScope = valueScope;
    }

    /**
     * 获取参数默认值
     *
     * @return default_value - 参数默认值
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * 设置参数默认值
     *
     * @param defaultValue 参数默认值
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", groupId=").append(groupId);
        sb.append(", name=").append(name);
        sb.append(", paramName=").append(paramName);
        sb.append(", type=").append(type);
        sb.append(", createTime=").append(createTime);
        sb.append(", valueScope=").append(valueScope);
        sb.append(", defaultValue=").append(defaultValue);
        sb.append("]");
        return sb.toString();
    }
}