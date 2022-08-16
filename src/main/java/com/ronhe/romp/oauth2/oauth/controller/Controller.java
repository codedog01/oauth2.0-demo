package com.ronhe.romp.oauth2.oauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>description goes here</p>
 *
 * @author 冷澳
 * @date 2022/8/16
 */
@RestController
@RequestMapping("/api")
public class Controller {
    @GetMapping("/test")
    public String test() {
        return "123312";
    }
}
