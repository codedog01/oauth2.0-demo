package com.ronhe.romp.oauth2.core.service.impl;

import com.ronhe.romp.oauth2.core.model.TokenModel;
import com.ronhe.romp.oauth2.core.service.AbstractOAuth2Service;
import com.ronhe.romp.oauth2.core.service.OAuth2Service;
import com.ronhe.romp.oauth2.core.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>授权码模式</p>
 *
 * @author 冷澳
 * @date 2022/8/16
 */
public class AuthorizationCodeService extends AbstractOAuth2Service {
    @Autowired
    TokenStore tokenStore;

    @Autowired
    TokenEndpoint tokenEndPoint;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    @Qualifier("jdbcClientDetailsService")
    private ClientDetailsService clientDetailsService;

    @Autowired
    public PasswordEncoder passwordEncoder;


    @Override
    public Result<TokenModel> checkToken(TokenModel token) {
        return null;
    }

    @Override
    public Result<OAuth2AccessToken> getToken(TokenModel tokenModel) {
        return null;
    }


}
