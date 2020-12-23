package com.zkwg.modelmanager.request;

import com.zkwg.modelmanager.entity.TrainingParameter;

import java.util.List;

public class TrainingRequest extends PageInfoRequest  {

    private String name;

    private Integer algorithmId;

    private String containerIds;

    private Integer dataSetId;

    private Float dataSetTrainingProportion;

    private Float dataSetValidationProportion;

    private List<TrainingParameter> trainingParameterList;

    private String remark;

    public TrainingRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAlgorithmId() {
        return algorithmId;
    }

    public void setAlgorithmId(Integer algorithmId) {
        this.algorithmId = algorithmId;
    }

    public String getContainerIds() {
        return containerIds;
    }

    public void setContainerIds(String containerIds) {
        this.containerIds = containerIds;
    }

    public Integer getDataSetId() {
        return dataSetId;
    }

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

    public List<TrainingParameter> getTrainingParameterList() {
        return trainingParameterList;
    }

    public void setTrainingParameterList(List<TrainingParameter> trainingParameterList) {
        this.trainingParameterList = trainingParameterList;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
