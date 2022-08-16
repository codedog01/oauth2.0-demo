package com.ronhe.romp.oauth2.core.service;

import com.ronhe.romp.oauth2.core.model.TokenModel;
import com.ronhe.romp.oauth2.core.service.impl.UserDetailServiceImpl;
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
 * <p>description goes here</p>
 *
 * @author 冷澳
 * @date 2022/8/16
 */
public abstract class AbstractOAuth2Service implements OAuth2Service {

    @Autowired
    TokenEndpoint tokenEndPoint;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Override
    public Result<OAuth2AccessToken> refreshToken(TokenModel tokenModel) {
        try {
            UserDetails userDetails = userDetailService.loadUserByUsername(tokenModel.getClientId());
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("grant_type", "refresh_token");
            parameters.put("refresh_token", tokenModel.getRefreshToken());
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(tokenModel.getClientId(), null, userDetails.getAuthorities());
            return Result.ofSuccess(tokenEndPoint.postAccessToken(token, parameters).getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ofThrowable(e);
        }
    }
}
