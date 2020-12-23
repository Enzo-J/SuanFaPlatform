package com.zkwg.modelmanager.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2020-10-19
 */
@Data
public class Datasource implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uuid;

    private Integer tenantId;

    private Integer id;

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 数据源类型 1：mysql 2:
     */
    private Integer type;

    @TableField(exist = false)
    private String host;

    @TableField(exist = false)
    private Integer port;

    @TableField(exist = false)
    private String database;

    /**
     * 数据源地址
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 链接参数
     */
    private String connectionParams;

    /**
     * 创建人
     */
    private Integer creator;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;


}
