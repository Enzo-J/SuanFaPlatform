package com.zkwg.modelmanager.utils;

import com.zkwg.modelmanager.response.ResultCode;

public class ResultUtil {
    
    /**
     * 成功无返回数据
     * @return
     */
    public static Result success() {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        return result;
    }
    
    /**
     * 成功带返回数据
     * @param data 响应数据
     * @return
     */
    public static Result success(Object data) {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    } 
    
    /**
     * 自定义状态码并带返回数据
     * @param code 结果状态码
     * @param data 响应数据
     * @return
     */
    public static Result success(ResultCode code, Object data) {
        Result result = new Result();
        result.setResultCode(code);
        result.setData(data);
        return result;
    }
    
    /**
     * 失败不带返回数据
     * @return
     */
    public static Result failure() {
        Result result = new Result();
        result.setResultCode(ResultCode.FAILURE);
        return result;
    }
    
    /**
     * 失败带返回数据
     * @param data 响应数据
     * @return
     */
    public static Result failure(Object data) {
        Result result = new Result();
        result.setResultCode(ResultCode.FAILURE);
        result.setData(data);
        return result;
    }

    public static Result failure(Object data,String msg) {
        Result result = new Result();
        result.setResultCode(ResultCode.FAILURE);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }
}