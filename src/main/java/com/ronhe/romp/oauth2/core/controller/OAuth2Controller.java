package com.ronhe.romp.oauth2.core.controller;

import com.ronhe.romp.oauth2.core.model.TokenModel;
import com.ronhe.romp.oauth2.core.service.OAuth2Service;
import com.ronhe.romp.oauth2.core.service.impl.AuthorizationCodeService;
import com.ronhe.romp.oauth2.core.service.impl.ClientCredentialsService;
import com.ronhe.romp.oauth2.core.service.impl.PasswordCredentialsService;
import com.ronhe.romp.oauth2.core.util.Result;
import com.ronhe.romp.oauth2.core.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>description goes here</p>
 *
 * @author 冷澳
 * @date 2022/8/14
 */
@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {
    @Autowired
    SpringContextUtils springContextUtils;


    /**
     * 所需参数
     * grantType
     * clientId
     * secret
     */
    @PostMapping("/getToken")
    public Result<OAuth2AccessToken> getToken(TokenModel tokenModel) {
        OAuth2Service oAuth2Service = this.getOAuth2Service(tokenModel.getGrantType());
        return oAuth2Service.getToken(tokenModel);
    }


    /**
     * header: Authorization:bearer token
     * 所需参数 token
     */
    @PostMapping("/checkToken")
    public Result<TokenModel> checkToken(TokenModel tokenModel) {
        OAuth2Service oAuth2Service = this.getOAuth2Service(tokenModel.getGrantType());
        return oAuth2Service.checkToken(tokenModel);
    }


    /**
     * header: Authorization:bearer token
     * 所需参数
     * refreshToken
     */
    @PostMapping("/refreshToken")
    public Result<OAuth2AccessToken> refreshToken(TokenModel tokenModel) {
        OAuth2Service oAuth2Service = this.getOAuth2Service(tokenModel.getGrantType());
        return oAuth2Service.refreshToken(tokenModel);
    }


    private OAuth2Service getOAuth2Service(String type) {
        OAuth2Service oAuth2Service = null;
        switch (type) {
            case "password":
                oAuth2Service= springContextUtils.getBean(PasswordCredentialsService.class);
            case "authorization_code":
                oAuth2Service= springContextUtils.getBean(AuthorizationCodeService.class);
            case "client_credentials":
                oAuth2Service= springContextUtils.getBean(ClientCredentialsService.class);
        }
        return oAuth2Service;
    }
}
