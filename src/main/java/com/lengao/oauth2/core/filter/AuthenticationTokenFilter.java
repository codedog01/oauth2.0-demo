package com.lengao.oauth2.core.filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>description goes here</p>
 * oauth2.0免登录页认证
 * @author 冷澳
 * @date 2022/8/4
 */
public class AuthenticationTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String account = httpServletRequest.getHeader("username");//账户
        String credentials = httpServletRequest.getHeader("password");//密码
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(account, credentials);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
