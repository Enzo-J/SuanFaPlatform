package com.zkwg.modelmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 */
@Data
public class DictItem   {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer dictId;

    private String text;

    private String value;

    private String remark;

    private Integer sort;

    /**
     * 1：启用 0：禁用
     */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
