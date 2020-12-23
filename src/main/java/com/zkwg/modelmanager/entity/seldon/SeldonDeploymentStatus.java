//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zkwg.modelmanager.entity.seldon;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.fabric8.kubernetes.api.model.KubernetesResource;
import io.fabric8.kubernetes.api.model.ServiceStatus;
import io.fabric8.kubernetes.api.model.apps.DeploymentCondition;
import io.fabric8.kubernetes.api.model.apps.DeploymentStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(Include.NON_NULL)
//@JsonPropertyOrder({"apiVersion", "kind", "metadata", "availableReplicas", "collisionCount", "conditions", "observedGeneration", "readyReplicas", "replicas", "unavailableReplicas", "updatedReplicas"})
@JsonDeserialize(
    using = None.class
)
public class SeldonDeploymentStatus implements KubernetesResource {

    @JsonProperty("state")
    private String state;
    @JsonProperty("description")
    private String description;
    @JsonProperty("deploymentStatus")
    private Map<String,DeploymentStatus> deploymentStatus;
    @JsonProperty("serviceStatus")
    private Map<String,ServiceStatus> serviceStatus;
    @JsonProperty("replicas")
    private Integer replicas;
    @JsonProperty("address")
    private Address address;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    private Byte status; // 1:正在运行 2：准备停止 3：停止  4.运行失败  5：发布  6：超时

    public SeldonDeploymentStatus() {
    }

    public SeldonDeploymentStatus(String state, String description, Map<String, DeploymentStatus> deploymentStatus, Map<String, ServiceStatus> serviceStatus, Integer replicas, Address address, Map<String, Object> additionalProperties) {
        this.state = state;
        this.description = description;
        this.deploymentStatus = deploymentStatus;
        this.serviceStatus = serviceStatus;
        this.replicas = replicas;
        this.address = address;
        this.additionalProperties = additionalProperties;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, DeploymentStatus> getDeploymentStatus() {
        return deploymentStatus;
    }

    public void setDeploymentStatus(Map<String, DeploymentStatus> deploymentStatus) {
        this.deploymentStatus = deploymentStatus;
    }

    public Map<String, ServiceStatus> getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(Map<String, ServiceStatus> serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public Integer getReplicas() {
        return replicas;
    }

    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
