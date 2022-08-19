package com.ronhe.romp.oauth2.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>description goes here</p>
 * 拦截oauth2异常，防止大量异常信息打印
 * @author 冷澳
 * @date 2022/8/17
 */
@ControllerAdvice
public class Oauth2ExceptionHandler {
    private final static Logger LOG= LoggerFactory.getLogger(Oauth2ExceptionHandler.class);

    @ExceptionHandler(value = OAuth2Exception.class)
    @ResponseBody
    public Object exceptionHandler(Exception e) {
        LOG.error(e.toString());
        return e;
    }
}
