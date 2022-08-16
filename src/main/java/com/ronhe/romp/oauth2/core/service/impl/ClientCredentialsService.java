package com.ronhe.romp.oauth2.core.service.impl;

import com.ronhe.romp.oauth2.core.model.TokenModel;
import com.ronhe.romp.oauth2.core.service.AbstractOAuth2Service;
import com.ronhe.romp.oauth2.core.service.OAuth2Service;
import com.ronhe.romp.oauth2.core.util.Result;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * <p>密码模式</p>
 *
 * @author 冷澳
 * @date 2022/8/16
 */
public class ClientCredentialsService extends AbstractOAuth2Service {

    @Override
    public Result<TokenModel> checkToken(TokenModel token) {
        return null;
    }

    @Override
    public Result<OAuth2AccessToken> getToken(TokenModel tokenModel) {
        return null;
    }

    @Override
    public Result<OAuth2AccessToken> refreshToken(TokenModel tokenModel) {
        return Result.ofFail("客户端模式不支持刷新token.");
    }
}
