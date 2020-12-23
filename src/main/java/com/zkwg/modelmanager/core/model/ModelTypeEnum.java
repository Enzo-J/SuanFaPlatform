package com.zkwg.modelmanager.core.model;

import com.zkwg.modelmanager.core.ProcessFactory;

public enum ModelTypeEnum {

    SKLEARN_SERVER_MODEL("SKLEARN_SERVER", "deploy/seldon_template.yml", ProcessFactory.getSeldonlModelProcess()),

    FLASK_MODEL("FLASK_SERVER", "deploy/flask_template.yml", ProcessFactory.getFlaskModelProcess());

    public String name;

    public String template;

    public ModelProcess modelProcess;

    ModelTypeEnum(String name, String template, ModelProcess modelProcess) {
        this.name = name;
        this.template = template;
        this.modelProcess = modelProcess;
    }

    public static ModelTypeEnum match(String name) {
        ModelTypeEnum[] values = ModelTypeEnum.values();
        for(ModelTypeEnum modelTypeEnum : values){
            if(modelTypeEnum.name.equals(name)){
                return modelTypeEnum;
            }
        }
        throw new RuntimeException("未找到匹配模型类型！");
    }

    public String getName() {
        return name;
    }

    public ModelProcess getModelProcess() {
        return modelProcess;
    }

    public String getTemplate() {
        return template;
    }
}
