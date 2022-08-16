package com.ronhe.romp.oauth2.core.service;

import com.ronhe.romp.oauth2.core.model.TokenModel;
import com.ronhe.romp.oauth2.core.util.Result;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.Map;

/**
 * <p>description goes here</p>
 *
 * @author 冷澳
 * @date 2022/8/14
 */
public interface OAuth2Service {
    Result<TokenModel> checkToken(TokenModel token);

    Result<OAuth2AccessToken> getToken(TokenModel tokenModel);

    Result<OAuth2AccessToken> refreshToken(TokenModel tokenModel);
}
