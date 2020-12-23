package com.zkwg.modelmanager.entity.seldon;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(
        using = JsonDeserializer.None.class
)
public class SvcOrchSpec implements KubernetesResource {

    @JsonProperty("Resources")
    private ResourceRequirements Resources;
    @JsonProperty("env")
    private EnvVar env;
    @JsonProperty("replicas")
    private Integer replicas;

//    Resources *v1.ResourceRequirements `json:"resources,omitempty" protobuf:"bytes,1,opt,name=resources"`
//    Env       []*v1.EnvVar             `json:"env,omitempty" protobuf:"bytes,2,opt,name=env"`
//    Replicas  *int32                   `json:"replicas,omitempty" protobuf:"bytes,3,opt,name=replicas"`

    public SvcOrchSpec() {
    }

    public ResourceRequirements getResources() {
        return Resources;
    }

    public void setResources(ResourceRequirements resources) {
        Resources = resources;
    }

    public EnvVar getEnv() {
        return env;
    }

    public void setEnv(EnvVar env) {
        this.env = env;
    }

    public Integer getReplicas() {
        return replicas;
    }

    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
    }
}
