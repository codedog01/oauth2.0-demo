package com.lengao.oauth2.core.aop;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

/**
 * <p>description goes here</p>
 *
 * @author 冷澳
 * @date 2022/8/17
 */
@Component
@Aspect
public class AuthAspectAround {
    private final static Logger LOG= LoggerFactory.getLogger(AuthAspectAround.class);


    @Autowired
    TokenStore tokenStore;

    @Pointcut(value = "execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken(..))")
    public void resetRefreshToken() {
    }

    @Pointcut(value = "execution(* org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint.authorize(..))")
    public void returnCode() {
    }

    @Around(value = "resetRefreshToken()")
    public Object resetRefreshToken(ProceedingJoinPoint point) {
        Object proceed = null;
        try {
            proceed = point.proceed();
            boolean contains = Arrays.toString(point.getArgs()).contains("grant_type=refresh_token");
            boolean access_token = JSON.toJSONString(proceed).contains("refreshToken");
            if (contains && access_token) {
                System.out.println(JSON.toJSONString(proceed));
                ResponseEntity<OAuth2AccessToken> responseEntity = (ResponseEntity<OAuth2AccessToken>) proceed;
                OAuth2AccessToken body = responseEntity.getBody();
                tokenStore.removeAccessTokenUsingRefreshToken(body.getRefreshToken());
                tokenStore.removeRefreshToken(body.getRefreshToken());
            }
        } catch (Throwable throwable) {
            LOG.error(throwable.getMessage());
        }
        return proceed ;
    }

    @Around(value = "returnCode()")
    public Object returnCode(ProceedingJoinPoint point) throws Throwable {
        Object proceed = point.proceed();
        ModelAndView modelAndView = (ModelAndView) proceed;
        if (modelAndView.hasView()) {
            throw OAuth2Exception.create("dasd", "modelAndView");
        }
        return proceed ;
    }
}
