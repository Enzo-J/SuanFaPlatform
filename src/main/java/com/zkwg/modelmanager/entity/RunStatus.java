package com.zkwg.modelmanager.entity;

public enum RunStatus {

        RUNNING(1, "运行中"),
        STOPING(2, "准备停止"),
        STOP(3, "停止"),
        FAILURE(4, "运行失败"),
        TIME_OUT(6, "超时");
        // 结果状态码
        private Integer code;

        // 结果消息
        private String message;

        RunStatus(Integer code, String message) {
                this.code = code;
                this.message = message;
        }

        public Integer getCode() {
                return code;
        }

        public String getMessage() {
                return message;
        }
}