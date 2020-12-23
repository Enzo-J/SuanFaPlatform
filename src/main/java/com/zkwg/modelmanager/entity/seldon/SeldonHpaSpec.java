package com.zkwg.modelmanager.entity.seldon;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.MetricSpec;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(
        using = JsonDeserializer.None.class
)
public class SeldonHpaSpec {

    @JsonProperty("minReplicas")
    private Integer minReplicas;
    @JsonProperty("maxReplicas")
    private Integer maxReplicas;
    @JsonProperty("metrics")
    private MetricSpec[] metrics;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
//    MinReplicas *int32                          `json:"minReplicas,omitempty" protobuf:"int,1,opt,name=minReplicas"`
//    MaxReplicas int32                           `json:"maxReplicas" protobuf:"int,2,opt,name=maxReplicas"`
//    Metrics[]autoscalingv2beta2.MetricSpec `json:"metrics,omitempty" protobuf:"bytes,3,opt,name=metrics"`

    public SeldonHpaSpec() {
    }

    public Integer getMinReplicas() {
        return minReplicas;
    }

    public void setMinReplicas(Integer minReplicas) {
        this.minReplicas = minReplicas;
    }

    public Integer getMaxReplicas() {
        return maxReplicas;
    }

    public void setMaxReplicas(Integer maxReplicas) {
        this.maxReplicas = maxReplicas;
    }

    public MetricSpec[] getMetrics() {
        return metrics;
    }

    public void setMetrics(MetricSpec[] metrics) {
        this.metrics = metrics;
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
