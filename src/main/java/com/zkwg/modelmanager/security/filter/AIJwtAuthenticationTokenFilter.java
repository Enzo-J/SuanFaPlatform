package com.zkwg.modelmanager.security.filter;

import com.zkwg.modelmanager.core.BaseContextHandler;
import com.zkwg.modelmanager.security.AISecurityUser;
import com.zkwg.modelmanager.service.IUserService;
import com.zkwg.modelmanager.utils.JwtUtil;
import com.zkwg.modelmanager.ws.WebSocketLogServer;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 *
 * @Description token过滤器来验证token有效性
 **/

@Component
public class AIJwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private IUserService userService;

    @Autowired
    private WebSocketLogServer webSocketLogServer;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        AISecurityUser securityUser = jwtUtil.getUserFromToken(request);
//        BaseContextHandler.setTenant(1);
        if (ObjectUtils.isNotEmpty(securityUser)){
            BaseContextHandler.setTenant(securityUser.getTenantId());
            BaseContextHandler.setUserId(securityUser.getUserId());
            Set<String> permissions = userService.findPermsByUserId(securityUser.getUserId());
            Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(permissions.toArray(new String[0]));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(securityUser, null, authorities);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            webSocketLogServer.sendAccessLogs(securityUser, request);
        }

        chain.doFilter(request, response);
    }


}
