package com.zkwg.modelmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
//import javax.persistence.*;

//@Table(name = "training_parameter")
public class TrainingParameter implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer tenantId;
//    @Column(name = "training_id")
    private Integer trainingId;

//    @Column(name = "algorithm_id")
    private Integer algorithmId;

//    @Column(name = "parameter_id")
    private Integer parameterId;

    /**
     * 参数名
     */
//    @Column(name = "param_name")
    private String paramName;

    /**
     * 参数值
     */
    private String value;

    private static final long serialVersionUID = 1L;

    /**
     * @return training_id
     */
    public Integer getTrainingId() {
        return trainingId;
    }

    /**
     * @param trainingId
     */
    public void setTrainingId(Integer trainingId) {
        this.trainingId = trainingId;
    }

    /**
     * @return algorithm_id
     */
    public Integer getAlgorithmId() {
        return algorithmId;
    }

    /**
     * @param algorithmId
     */
    public void setAlgorithmId(Integer algorithmId) {
        this.algorithmId = algorithmId;
    }

    /**
     * @return parameter_id
     */
    public Integer getParameterId() {
        return parameterId;
    }

    /**
     * @param parameterId
     */
    public void setParameterId(Integer parameterId) {
        this.parameterId = parameterId;
    }

    /**
     * 获取参数名
     *
     * @return param_name - 参数名
     */
    public String getParamName() {
        return paramName;
    }

    /**
     * 设置参数名
     *
     * @param paramName 参数名
     */
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    /**
     * 获取参数值
     *
     * @return value - 参数值
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置参数值
     *
     * @param value 参数值
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", trainingId=").append(trainingId);
        sb.append(", algorithmId=").append(algorithmId);
        sb.append(", parameterId=").append(parameterId);
        sb.append(", paramName=").append(paramName);
        sb.append(", value=").append(value);
        sb.append("]");
        return sb.toString();
    }
}