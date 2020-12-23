package com.zkwg.modelmanager.utils;

import com.zkwg.modelmanager.response.ResultCode;

public class Result {

    // 结果状态码
    private Integer code;
    // 结果消息
    private String msg;
    // 结果数据
    private Object data;
 
    public Result() {
        super();
    }
 
    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    
    /**
     * 根据状态码枚举设置状态码和状态信息的值
     * @param code 状态码枚举
     */
    public void setResultCode(ResultCode code) {
        this.code = code.getCode();
        this.msg = code.getMessage();
    }
 
    public Integer getCode() {
        return code;
    }
 
    public void setCode(Integer code) {
        this.code = code;
    }
 
    public String getMsg() {
        return msg;
    }
 
    public void setMsg(String msg) {
        this.msg = msg;
    }
 
    public Object getData() {
        return data;
    }
 
    public void setData(Object data) {
        this.data = data;
    }
 
    @Override
    public String toString() {
        return "Result{" +
                "code=" + this.code +
                ", msg='" + this.msg + '\'' +
                ", data=" + this.data +
              '}';
    }
}