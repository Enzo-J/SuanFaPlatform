package com.zkwg.modelmanager.core;

import io.fabric8.kubernetes.api.model.ServicePort;

import java.util.List;
import java.util.Map;

public class ApiDoc {

    private ServicePort servicePort;

    private Route route;

    public ApiDoc() {
    }

    public ServicePort getServicePort() {
        return servicePort;
    }

    public void setServicePort(ServicePort servicePort) {
        this.servicePort = servicePort;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public class Route {

        private String predictions;

        private String doc;

        public Route() {
        }

        public String getPredictions() {
            return predictions;
        }

        public void setPredictions(String predictions) {
            this.predictions = predictions;
        }

        public String getDoc() {
            return doc;
        }

        public void setDoc(String doc) {
            this.doc = doc;
        }
    }

}
