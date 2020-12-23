package com.zkwg.modelmanager.entity.seldon;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.Container;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(
        using = JsonDeserializer.None.class
)
public class Explainer {

    @JsonProperty("type")
    private String type;
    @JsonProperty("modelUri")
    private String modelUri;
    @JsonProperty("serviceAccountName")
    private String serviceAccountName;
    @JsonProperty("containerSpec")
    private Container containerSpec;
    @JsonProperty("config")
    private Map<String, String> config;
    @JsonProperty("endpoint")
    private Endpoint endpoint;
    @JsonProperty("envSecretRefName")
    private String envSecretRefName;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

//    Type               AlibiExplainerType `json:"type,omitempty" protobuf:"string,1,opt,name=type"`
//    ModelUri           string             `json:"modelUri,omitempty" protobuf:"string,2,opt,name=modelUri"`
//    ServiceAccountName string             `json:"serviceAccountName,omitempty" protobuf:"string,3,opt,name=serviceAccountName"`
//    ContainerSpec      v1.Container       `json:"containerSpec,omitempty" protobuf:"bytes,4,opt,name=containerSpec"`
//    Config             map[string]string  `json:"config,omitempty" protobuf:"bytes,5,opt,name=config"`
//    Endpoint           *Endpoint          `json:"endpoint,omitempty" protobuf:"bytes,6,opt,name=endpoint"`
//    EnvSecretRefName   string             `json:"envSecretRefName,omitempty" protobuf:"bytes,7,opt,name=envSecretRefName"`

    public Explainer() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModelUri() {
        return modelUri;
    }

    public void setModelUri(String modelUri) {
        this.modelUri = modelUri;
    }

    public String getServiceAccountName() {
        return serviceAccountName;
    }

    public void setServiceAccountName(String serviceAccountName) {
        this.serviceAccountName = serviceAccountName;
    }

    public Container getContainerSpec() {
        return containerSpec;
    }

    public void setContainerSpec(Container containerSpec) {
        this.containerSpec = containerSpec;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public String getEnvSecretRefName() {
        return envSecretRefName;
    }

    public void setEnvSecretRefName(String envSecretRefName) {
        this.envSecretRefName = envSecretRefName;
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
