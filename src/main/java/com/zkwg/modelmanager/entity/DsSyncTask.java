package com.zkwg.modelmanager.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2020-10-22
 */
@Data
public class DsSyncTask {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer tenantId;

    private Integer processDefId;

    private Integer processInstanceId;

    private Integer taskInstanceId;

    /**
     * 源数据数据源uuid
     */
    private String source;

    /**
     * 目标数据源uuid
     */
    private String target;

    private String tables;

    private Integer status;

    public DsSyncTask(Integer processDefId, Integer processInstanceId, Integer taskInstanceId, String source, String target, String tables) {
        this.processDefId = processDefId;
        this.processInstanceId = processInstanceId;
        this.taskInstanceId = taskInstanceId;
        this.source = source;
        this.target = target;
        this.tables = tables;
    }
}
