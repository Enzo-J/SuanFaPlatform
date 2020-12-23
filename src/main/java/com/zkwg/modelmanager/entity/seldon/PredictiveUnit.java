package com.zkwg.modelmanager.entity.seldon;


import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.KubernetesResource;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(
        using = None.class
)
public class PredictiveUnit implements KubernetesResource {

    @JsonProperty("name")
    private String name;
    @JsonProperty("children")
    private PredictiveUnit[] children;
    @JsonProperty("type")
    private String type;
    @JsonProperty("implementation")
    private String implementation;
    @JsonProperty("methods")
    private String[] methods;
    @JsonProperty("endpoint")
    private Endpoint endpoint;
    @JsonProperty("parameters")
    private Parameter[] parameters;
    @JsonProperty("modelUri")
    private String modelUri;
    @JsonProperty("serviceAccountName")
    private String serviceAccountName;
    @JsonProperty("envSecretRefName")
    private String envSecretRefName;
    @JsonProperty("logger")
    private Logger logger;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap();

//    Name               string                        `json:"name" protobuf:"string,1,opt,name=name"`
//    Children           []PredictiveUnit              `json:"children,omitempty" protobuf:"bytes,2,opt,name=children"`
//    Type               *PredictiveUnitType           `json:"type,omitempty" protobuf:"int,3,opt,name=type"`
//    Implementation     *PredictiveUnitImplementation `json:"implementation,omitempty" protobuf:"int,4,opt,name=implementation"`
//    Methods            *[]PredictiveUnitMethod       `json:"methods,omitempty" protobuf:"int,5,opt,name=methods"`
//    Endpoint           *Endpoint                     `json:"endpoint,omitempty" protobuf:"bytes,6,opt,name=endpoint"`
//    Parameters         []Parameter                   `json:"parameters,omitempty" protobuf:"bytes,7,opt,name=parameters"`
//    ModelURI           string                        `json:"modelUri,omitempty" protobuf:"bytes,8,opt,name=modelUri"`
//    ServiceAccountName string                        `json:"serviceAccountName,omitempty" protobuf:"bytes,9,opt,name=serviceAccountName"`
//    EnvSecretRefName   string                        `json:"envSecretRefName,omitempty" protobuf:"bytes,10,opt,name=envSecretRefName"`
//    // Request/response  payload logging. v2alpha1 feature that is added to v1 for backwards compatibility while v1 is the storage version.
//    Logger *Logger `json:"logger,omitempty"`

    public PredictiveUnit() {
    }

    public PredictiveUnit(String name, PredictiveUnit[] children, String type, String implementation, String[] methods, Endpoint endpoint, Parameter[] parameters, String modelUri, String serviceAccountName, String envSecretRefName, Logger logger) {
        this.name = name;
        this.children = children;
        this.type = type;
        this.implementation = implementation;
        this.methods = methods;
        this.endpoint = endpoint;
        this.parameters = parameters;
        this.modelUri = modelUri;
        this.serviceAccountName = serviceAccountName;
        this.envSecretRefName = envSecretRefName;
        this.logger = logger;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PredictiveUnit[] getChildren() {
        return children;
    }

    public void setChildren(PredictiveUnit[] children) {
        this.children = children;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImplementation() {
        return implementation;
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    public String[] getMethods() {
        return methods;
    }

    public void setMethods(String[] methods) {
        this.methods = methods;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public void setParameters(Parameter[] parameters) {
        this.parameters = parameters;
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

    public String getEnvSecretRefName() {
        return envSecretRefName;
    }

    public void setEnvSecretRefName(String envSecretRefName) {
        this.envSecretRefName = envSecretRefName;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
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
