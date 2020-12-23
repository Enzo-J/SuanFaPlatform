package com.zkwg.modelmanager.security.handler;

import com.zkwg.modelmanager.response.R;
import com.zkwg.modelmanager.response.ResultUtil;
import com.zkwg.modelmanager.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 用来解决认证过的用户访问无权限资源时的异常
 * @author lihaodong
 */
@Slf4j
public class AIAuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        log.error("请求访问: " + request.getRequestURI() + " 接口， 经jwt认证失败，无法访问系统资源.");
        SecurityUtil.writeJavaScript(ResultUtil.failure(9000,"请求访问:" + request.getRequestURI() + "接口,经jwt 认证失败,无法访问系统资源"),response);
    }
}
