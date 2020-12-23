package com.zkwg.modelmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;
//import javax.persistence.*;

public class Training implements Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

//    @Column(name = "user_id")
    private Integer tenantId;

    /**
     * 创建人
     */
    private Integer creator;

    /**
     * 训练作业名称
     */
    private String name;

    /**
     * 数据集id
     */
//    @Column(name = "data_set_id")
    private Integer dataSetId;

    /**
     * 训练集比例
     */
    private Float dataSetTrainingProportion;

    /**
     * 验证集比例
     */
    private Float dataSetValidationProportion;

    /**
     * 容器id
     */
//    @Column(name = "container_ids")
    private String containerIds;

    /**
     * 算法id
     */
//    @Column(name = "algorithm_id")
    private Integer algorithmId;

//    @Column(name = "job_json_md5")
    private String jobJsonMd5;

    /**
     * 状态 1:已创建 2：等待运行 3：运行中 4：运行失败 5：运行成功 6：删除
     */
    private Byte status;

    /**
     * 创建时间
     */
//    @Column(name = "create_time")
    private Date createTime;

    /**
     * 开始时间
     */
//    @Column(name = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
//    @Column(name = "end_time")
    private Date endTime;

    /**
     * 描述
     */
    private String remark;

//    @Column(name = "is_delete")
    private Byte isDelete;

//    @Column(name = "job_json")
    private String jobJson;

//    @Column(name = "runing_job_json")
    private String runingJobJson;

    private String modelId;

    /**
     * 训练参数
     */
    private String parameters;

    private static final long serialVersionUID = 1L;

    public Training() {
    }

    public Training(String jobJsonMd5) {
        this.jobJsonMd5 = jobJsonMd5;
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

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    /**
     * 获取训练作业名称
     *
     * @return name - 训练作业名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置训练作业名称
     *
     * @param name 训练作业名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取数据集id
     *
     * @return data_set_id - 数据集id
     */
    public Integer getDataSetId() {
        return dataSetId;
    }

    /**
     * 设置数据集id
     *
     * @param dataSetId 数据集id
     */
    public void setDataSetId(Integer dataSetId) {
        this.dataSetId = dataSetId;
    }

    public Float getDataSetTrainingProportion() {
        return dataSetTrainingProportion;
    }

    public void setDataSetTrainingProportion(Float dataSetTrainingProportion) {
        this.dataSetTrainingProportion = dataSetTrainingProportion;
    }

    public Float getDataSetValidationProportion() {
        return dataSetValidationProportion;
    }

    public void setDataSetValidationProportion(Float dataSetValidationProportion) {
        this.dataSetValidationProportion = dataSetValidationProportion;
    }

    /**
     * 获取容器id
     *
     * @return container_ids - 容器id
     */
    public String getContainerIds() {
        return containerIds;
    }

    /**
     * 设置容器id
     *
     * @param containerIds 容器id
     */
    public void setContainerIds(String containerIds) {
        this.containerIds = containerIds;
    }

    /**
     * 获取算法id
     *
     * @return algorithm_id - 算法id
     */
    public Integer getAlgorithmId() {
        return algorithmId;
    }

    /**
     * 设置算法id
     *
     * @param algorithmId 算法id
     */
    public void setAlgorithmId(Integer algorithmId) {
        this.algorithmId = algorithmId;
    }

    /**
     * @return job_json_md5
     */
    public String getJobJsonMd5() {
        return jobJsonMd5;
    }

    /**
     * @param jobJsonMd5
     */
    public void setJobJsonMd5(String jobJsonMd5) {
        this.jobJsonMd5 = jobJsonMd5;
    }

    /**
     * 获取状态 1:已创建 2：等待运行 3：运行中 4：运行失败 5：运行成功 6：删除
     *
     * @return status - 状态 1:已创建 2：等待运行 3：运行中 4：运行失败 5：运行成功 6：删除
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态 1:已创建 2：等待运行 3：运行中 4：运行失败 5：运行成功 6：删除
     *
     * @param status 状态 1:已创建 2：等待运行 3：运行中 4：运行失败 5：运行成功 6：删除
     */
    public void setStatus(Byte status) {
        this.status = status;
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
     * 获取开始时间
     *
     * @return start_time - 开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取结束时间
     *
     * @return end_time - 结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置结束时间
     *
     * @param endTime 结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取描述
     *
     * @return remark - 描述
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置描述
     *
     * @param remark 描述
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return is_delete
     */
    public Byte getIsDelete() {
        return isDelete;
    }

    /**
     * @param isDelete
     */
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * @return job_json
     */
    public String getJobJson() {
        return jobJson;
    }

    /**
     * @param jobJson
     */
    public void setJobJson(String jobJson) {
        this.jobJson = jobJson;
    }

    /**
     * @return runing_job_json
     */
    public String getRuningJobJson() {
        return runingJobJson;
    }

    /**
     * @param runingJobJson
     */
    public void setRuningJobJson(String runingJobJson) {
        this.runingJobJson = runingJobJson;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", creator=").append(creator);
        sb.append(", name=").append(name);
        sb.append(", dataSetId=").append(dataSetId);
        sb.append(", containerIds=").append(containerIds);
        sb.append(", algorithmId=").append(algorithmId);
        sb.append(", jobJsonMd5=").append(jobJsonMd5);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", remark=").append(remark);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", jobJson=").append(jobJson);
        sb.append(", runingJobJson=").append(runingJobJson);
        sb.append(", modelId=").append(modelId);
        sb.append(", parameters=").append(parameters);
        sb.append("]");
        return sb.toString();
    }
}