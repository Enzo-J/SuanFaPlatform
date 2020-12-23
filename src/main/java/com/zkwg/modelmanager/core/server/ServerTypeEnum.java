package com.zkwg.modelmanager.core.server;

import com.zkwg.modelmanager.core.ProcessFactory;

public enum ServerTypeEnum {

    SKLEARN_SERVER("SKLEARN_SERVER", ProcessFactory.getSeldonlServerProcess()),

    FLASK_SERVER("FLASK_SERVER", ProcessFactory.getFlaskServerProcess());

    public String name;

    public ServerProcess serverProcess;

    ServerTypeEnum(String name, ServerProcess serverProcess) {
        this.name = name;
        this.serverProcess = serverProcess;
    }

    public static ServerTypeEnum match(String name) {
        ServerTypeEnum[] values = ServerTypeEnum.values();
        for(ServerTypeEnum modelTypeEnum : values){
            if(modelTypeEnum.name.equals(name)){
                return modelTypeEnum;
            }
        }
        throw new RuntimeException("未找到匹配服务类型！");
    }

    public String getName() {
        return name;
    }

    public ServerProcess getServerProcess() {
        return serverProcess;
    }

}
