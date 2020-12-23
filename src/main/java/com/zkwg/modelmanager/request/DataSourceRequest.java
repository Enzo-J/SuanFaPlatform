package com.zkwg.modelmanager.request;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class DataSourceRequest extends PageInfoRequest {

    private String uuid;

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 数据源类型 1：mysql 2:
     */
    private Integer type;

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

    private String host;

    private Integer port;

    private String database;

    /**
     * 备注
     */
    private String remark;

    public DataSourceRequest() {
    }
}
