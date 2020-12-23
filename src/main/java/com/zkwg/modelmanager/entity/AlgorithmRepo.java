package com.zkwg.modelmanager.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class AlgorithmRepo implements Serializable {

    private Integer id;

    @TableField(value = "first_class_name")
    private String firstClassName;

    @TableField(value = "first_class_name_en")
    private String firstClassNameEN;

    @TableField(value = "second_class_name")
    private String secondClassName;

    @TableField(value = "second_class_name_en")
    private String secondClassNameEN;

    private List<AlgorithmParameter> algorithmParameterList;

//    @TableField(value = "name")
//    private String parameterName;
//
//    @TableField(value = "value_scope")
//    private String valueScope;
//
//    @TableField(value = "default_value")
//    private String defaultValue;

    private static final long serialVersionUID = 1L;


}