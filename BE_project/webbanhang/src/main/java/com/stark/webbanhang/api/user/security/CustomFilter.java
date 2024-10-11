package com.stark.webbanhang.api.user.security;

import com.stark.webbanhang.api.user.dto.request.IntrospectRequest;
import com.stark.webbanhang.api.user.dto.response.IntrospectResponse;
import com.stark.webbanhang.api.user.service.impl.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class CustomFilter extends OncePerRequestFilter {
    @Autowired
    AuthenticationService authenticationService;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromHeader(request);
        IntrospectRequest introspectRequest = new IntrospectRequest();
        introspectRequest.setToken(token);
        if (token != null){
            IntrospectResponse introspectResponse = authenticationService.introspect(introspectRequest);
            if(introspectResponse.isValid()){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken("","",new ArrayList<>());
                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request,response);
    }
    private String getTokenFromHeader(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String token = null;
        if(StringUtils.hasText(header) && header.startsWith("Bearer")){
            token = header.substring(7);// cắt từ chuỗi số 7 tuc là cắt sau chứ Bearer
        }
        return token;
    }

}
