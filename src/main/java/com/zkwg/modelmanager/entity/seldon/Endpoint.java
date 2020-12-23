package com.zkwg.modelmanager.entity.seldon;

import io.fabric8.kubernetes.api.model.ServicePort;

public class Endpoint {

    private String service_host;

    private Integer service_port;

    private String type;

//    ServiceHost string       `json:"service_host,omitempty" protobuf:"string,1,opt,name=service_host"`
//    ServicePort int32        `json:"service_port,omitempty" protobuf:"int32,2,opt,name=service_port"`
//    Type        EndpointType `json:"type,omitempty" protobuf:"int,3,opt,name=type"`

    public Endpoint() {
    }

    public String getService_host() {
        return service_host;
    }

    public void setService_host(String service_host) {
        this.service_host = service_host;
    }

    public Integer getService_port() {
        return service_port;
    }

    public void setService_port(Integer service_port) {
        this.service_port = service_port;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
