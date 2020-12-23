package com.zkwg.modelmanager.entity.seldon;

public class Logger {

    private String url;

    private String mode;

//    Url *string `json:"url,omitempty"`
//    // What payloads to log
//    Mode LoggerMode `json:"mode,omitempty"`


    public Logger() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
