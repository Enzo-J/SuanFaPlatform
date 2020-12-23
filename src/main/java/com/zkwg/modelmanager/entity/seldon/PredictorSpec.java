package com.zkwg.modelmanager.entity.seldon;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.ResourceRequirements;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(
        using = JsonDeserializer.None.class
)
public class PredictorSpec implements KubernetesResource {

    @JsonProperty("name")
    private String name;
    @JsonProperty("graph")
    private PredictiveUnit graph;
    @JsonProperty("componentSpecs")
    private SeldonPodSpec[] componentSpecs;
    @JsonProperty("replicas")
    private Integer replicas;
    @JsonProperty("annotations")
    private Map<String, String> annotations;
    @JsonProperty("engineResources")
    private ResourceRequirements engineResources;
    @JsonProperty("labels")
    private Map<String, String> labels;
    @JsonProperty("svcOrchSpec")
    private SvcOrchSpec svcOrchSpec;
    @JsonProperty("traffic")
    private Integer traffic;
    @JsonProperty("explainer")
    private Explainer explainer;
    @JsonProperty("shadow")
    private Boolean shadow;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

//    Name            string                  `json:"name" protobuf:"string,1,opt,name=name"`
//    Graph           PredictiveUnit          `json:"graph" protobuf:"bytes,2,opt,name=predictiveUnit"`
//    ComponentSpecs  []*SeldonPodSpec        `json:"componentSpecs,omitempty" protobuf:"bytes,3,opt,name=componentSpecs"`
//    Replicas        *int32                  `json:"replicas,omitempty" protobuf:"string,4,opt,name=replicas"`
//    Annotations     map[string]string       `json:"annotations,omitempty" protobuf:"bytes,5,opt,name=annotations"`
//    EngineResources v1.ResourceRequirements `json:"engineResources,omitempty" protobuf:"bytes,6,opt,name=engineResources"`
//    Labels          map[string]string       `json:"labels,omitempty" protobuf:"bytes,7,opt,name=labels"`
//    SvcOrchSpec     SvcOrchSpec             `json:"svcOrchSpec,omitempty" protobuf:"bytes,8,opt,name=svcOrchSpec"`
//    Traffic         int32                   `json:"traffic,omitempty" protobuf:"bytes,9,opt,name=traffic"`
//    Explainer       *Explainer              `json:"explainer,omitempty" protobuf:"bytes,10,opt,name=explainer"`
//    Shadow          bool                    `

    public PredictorSpec() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PredictiveUnit getGraph() {
        return graph;
    }

    public void setGraph(PredictiveUnit graph) {
        this.graph = graph;
    }

    public SeldonPodSpec[] getComponentSpecs() {
        return componentSpecs;
    }

    public void setComponentSpecs(SeldonPodSpec[] componentSpecs) {
        this.componentSpecs = componentSpecs;
    }

    public Integer getReplicas() {
        return replicas;
    }

    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
    }

    public Map<String, String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Map<String, String> annotations) {
        this.annotations = annotations;
    }

    public ResourceRequirements getEngineResources() {
        return engineResources;
    }

    public void setEngineResources(ResourceRequirements engineResources) {
        this.engineResources = engineResources;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public SvcOrchSpec getSvcOrchSpec() {
        return svcOrchSpec;
    }

    public void setSvcOrchSpec(SvcOrchSpec svcOrchSpec) {
        this.svcOrchSpec = svcOrchSpec;
    }

    public Integer getTraffic() {
        return traffic;
    }

    public void setTraffic(Integer traffic) {
        this.traffic = traffic;
    }

    public Explainer getExplainer() {
        return explainer;
    }

    public void setExplainer(Explainer explainer) {
        this.explainer = explainer;
    }

    public Boolean getShadow() {
        return shadow;
    }

    public void setShadow(Boolean shadow) {
        this.shadow = shadow;
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
