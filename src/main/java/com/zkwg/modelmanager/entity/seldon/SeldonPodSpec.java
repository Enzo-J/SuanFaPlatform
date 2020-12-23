package com.zkwg.modelmanager.entity.seldon;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.PodSpec;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(
        using = JsonDeserializer.None.class
)
public class SeldonPodSpec implements KubernetesResource {

    @JsonProperty("metadata")
    private ObjectMeta metadata;

    @JsonProperty("spec")
    private PodSpec spec;

    @JsonProperty("hpaSpec")
    private SeldonHpaSpec hpaSpec;

    @JsonProperty("name")
    private Integer replicas;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

//    Metadata metav1.ObjectMeta `json:"metadata,omitempty" protobuf:"bytes,1,opt,name=metadata"`
//    Spec     v1.PodSpec        `json:"spec,omitempty" protobuf:"bytes,2,opt,name=spec"`
//    HpaSpec  *SeldonHpaSpec    `json:"hpaSpec,omitempty" protobuf:"bytes,3,opt,name=hpaSpec"`
//    Replicas *int32            `json:"replicas,omitempty" protobuf:"bytes,4,opt,name=replicas"`

    public SeldonPodSpec() {
    }

    public ObjectMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(ObjectMeta metadata) {
        this.metadata = metadata;
    }

    public PodSpec getSpec() {
        return spec;
    }

    public void setSpec(PodSpec spec) {
        this.spec = spec;
    }

    public SeldonHpaSpec getHpaSpec() {
        return hpaSpec;
    }

    public void setHpaSpec(SeldonHpaSpec hpaSpec) {
        this.hpaSpec = hpaSpec;
    }

    public Integer getReplicas() {
        return replicas;
    }

    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
