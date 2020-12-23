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
import io.fabric8.kubernetes.api.model.LabelSelector;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.apps.DeploymentStrategy;
import okhttp3.Protocol;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(Include.NON_NULL)
//@JsonPropertyOrder({"apiVersion", "kind", "metadata", "minReadySeconds", "paused", "progressDeadlineSeconds", "replicas", "revisionHistoryLimit", "selector", "strategy", "template"})
@JsonDeserialize(
        using = None.class
)
public class SeldonDeploymentSpec implements KubernetesResource {

    /**
     *
     *
     */
    @JsonProperty("name")
    private String name;
    /**
     *
     *
     */
    @JsonProperty("predictors")
    private PredictorSpec[] predictors;
    /**
     *
     *
     */
    @JsonProperty("oauthKey")
    private String oauthKey;
    /**
     *
     *
     */
    @JsonProperty("oauthSecret")
    private String oauthSecret;
    /**
     *
     *
     */
    @JsonProperty("annotations")
    private Map<String, String> annotations;
    /**
     *
     *
     */
    @JsonProperty("protocol")
    private Protocol protocol;
    /**
     *
     *
     */
    @JsonProperty("transport")
    private String transport;
    /**
     *
     *
     */
    @JsonProperty("replicas")
    private Integer replicas;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public SeldonDeploymentSpec() {
    }

    public SeldonDeploymentSpec(String name, PredictorSpec[] predictors, String oauthKey, String oauthSecret, Map<String, String> annotations, Protocol protocol, String transport, Integer replicas, Map<String, Object> additionalProperties) {
        this.name = name;
        this.predictors = predictors;
        this.oauthKey = oauthKey;
        this.oauthSecret = oauthSecret;
        this.annotations = annotations;
        this.protocol = protocol;
        this.transport = transport;
        this.replicas = replicas;
        this.additionalProperties = additionalProperties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PredictorSpec[] getPredictors() {
        return predictors;
    }

    public void setPredictors(PredictorSpec[] predictors) {
        this.predictors = predictors;
    }

    public String getOauthKey() {
        return oauthKey;
    }

    public void setOauthKey(String oauthKey) {
        this.oauthKey = oauthKey;
    }

    public String getOauthSecret() {
        return oauthSecret;
    }

    public void setOauthSecret(String oauthSecret) {
        this.oauthSecret = oauthSecret;
    }

    public Map<String, String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Map<String, String> annotations) {
        this.annotations = annotations;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public Integer getReplicas() {
        return replicas;
    }

    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
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

}
