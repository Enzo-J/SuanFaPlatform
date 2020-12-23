package com.zkwg.modelmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
public class DataSet {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer tenantId;

    private Integer creator;

    /**
     * 数据集名称
     */
    private String name;

    /**
     * 数据集类型 1:文本 2：图片
     */
    private Integer type;

    /**
     * 数据来源 1：数据库 2：文件系统
     */
    private Integer comeFrom;

    /**
     * 同步状态 1：
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 源数据数据源uuid
     */
    private String source;

    /**
     * 目标数据源uuid
     */
    private String target;

    private Integer dsSyncTaskId;

    /**
     * 备注
     */
    private String remark;

    private Integer isDelete;
}
