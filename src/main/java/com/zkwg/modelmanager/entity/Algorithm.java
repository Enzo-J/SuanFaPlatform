package com.zkwg.modelmanager.entity;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@Data
@TableName("`algorithm`")
public class Algorithm  {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer tenantId;

    /**
     * 创建人
     */
    private Integer creator;

    /**
     * 算法名称
     */
    private String name;

    /**
     * 分类
     */
    private Integer algTypeId;

    /**
     * 版本
     */
    private String version;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 描述
     */
    private String remark;

    /**
     * 图片
     */
    private String pictrue;

    private String image;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 是否公开
     */
    private Integer isPublish;


}
