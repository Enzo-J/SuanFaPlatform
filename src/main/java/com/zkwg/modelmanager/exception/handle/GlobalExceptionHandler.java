//package com.zkwg.modelmanager.exception.handle;
//
//import com.zkwg.modelmanager.controller.ExcelManagerController;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//
//    private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//
//    /**
//     * 没有登录
//     * @param request
//     * @param response
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(NoLoginException.class)
//    public Object noLoginExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e)
//    {
//        log.error("[GlobalExceptionHandler][noLoginExceptionHandler] exception",e);
//        JsonResult jsonResult = new JsonResult();
//        jsonResult.setCode(JsonResultCode.NO_LOGIN);
//        jsonResult.setMessage("用户登录失效或者登录超时,请先登录");
//        return jsonResult;
//    }
//
//    /**
//     *  业务异常
//     * @param request
//     * @param response
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(ServiceException.class)
//    public Object businessExceptionHandler(HttpServletRequest request,HttpServletResponse response,Exception e)
//    {
//        log.error("[GlobalExceptionHandler][businessExceptionHandler] exception",e);
//        JsonResult jsonResult = new JsonResult();
//        jsonResult.setCode(JsonResultCode.FAILURE);
//        jsonResult.setMessage("业务异常,请联系管理员");
//        return jsonResult;
//    }
//
//    /**
//     * 全局异常处理
//     * @param request
//     * @param response
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(Exception.class)
//    public Object exceptionHandler(HttpServletRequest request,HttpServletResponse response,Exception e)
//    {
//        log.error("[GlobalExceptionHandler][exceptionHandler] exception",e);
//        JsonResult jsonResult = new JsonResult();
//        jsonResult.setCode(JsonResultCode.FAILURE);
//        jsonResult.setMessage("系统错误,请联系管理员");
//        return jsonResult;
//    }
//}