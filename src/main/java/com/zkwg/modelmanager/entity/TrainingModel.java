package com.zkwg.modelmanager.entity;

import java.io.Serializable;

public class TrainingModel implements Serializable {

    private String model_id;

    private byte[] model;

    private String description;

    private static final long serialVersionUID = 1L;

    public TrainingModel()
    {

    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public byte[] getModel() {
        return model;
    }

    public void setModel(byte[] model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
