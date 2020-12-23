package com.zkwg.modelmanager.response;

import com.zkwg.modelmanager.utils.Result;

public class ResultUtil {
    
    /**
     * 成功无返回数据
     * @return
     */
    public static R success() {
        R result = new R();
        result.setResultCode(ResultCode.SUCCESS);
        return result;
    }
    
    /**
     * 成功带返回数据
     * @param data 响应数据
     * @return
     */
    public static R success(Object data) {
        R result = new R();
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

    public static R failure(Object data,String msg) {
        R result = new R();
        result.setResultCode(ResultCode.FAILURE);
        result.setData(data);
        result.setMessage(msg);
        return result;
    }
}