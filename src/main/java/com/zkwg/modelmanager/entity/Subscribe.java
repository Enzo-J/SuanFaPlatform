package com.zkwg.modelmanager.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2020-09-22
 */
@Data
public class Subscribe  {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 被订阅实体id
     */
    private Integer targetId;

    /**
     * 被订阅类型
     */
    private Integer targetType;

    private Integer creator;

    /**
     * 订阅时间
     */
    private LocalDateTime createTime;

    /**
     * 取消订阅时间
     */
    private LocalDateTime cancelTime;

    /**
     * 是否可用
     */
    private Integer flag;


}
