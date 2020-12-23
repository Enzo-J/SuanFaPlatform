package com.zkwg.modelmanager.utils;

import com.alibaba.fastjson.JSON;
import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.security.AISecurityUser;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Classname SecurityUtil
 * @Description 安全服务工具类
 */
@UtilityClass
public class SecurityUtil {

    public void writeJavaScript(R r, HttpServletResponse response) throws IOException {
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(JSON.toJSONString(r));
        printWriter.flush();
    }

    /**
     * 获取Authentication
     */
    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * @Description 获取用户
     * @Date 11:29 2019-05-10
     **/
    public AISecurityUser getUser(){
        try {
            return (AISecurityUser) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new RuntimeException("登录状态过期", e);
        }
    }
}
