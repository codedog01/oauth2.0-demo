package com.ronhe.romp.oauth2.core.service.impl;

import com.ronhe.romp.oauth2.core.model.TokenModel;
import com.ronhe.romp.oauth2.core.service.AbstractOAuth2Service;
import com.ronhe.romp.oauth2.core.service.OAuth2Service;
import com.ronhe.romp.oauth2.core.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>密码模式</p>
 *
 * @author 冷澳
 * @date 2022/8/16
 */
public class PasswordCredentialsService extends AbstractOAuth2Service {
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


    public Result<TokenModel> checkToken(TokenModel tokenModel) {
        OAuth2AccessToken oAuth2AccessToken;
        OAuth2Authentication oAuth2Authentication;
        //捕捉并返回报错信息
        try {
            oAuth2AccessToken = tokenStore.readAccessToken(tokenModel.getToken());
            oAuth2Authentication = tokenStore.readAuthentication(tokenModel.getToken());
        } catch (Exception e) {
            return Result.ofThrowable(e);
        }
        //封装返回对象
        if (oAuth2AccessToken != null && oAuth2AccessToken.getExpiresIn() > 0 && oAuth2Authentication != null) {
            TokenModel checkTokenModel = new TokenModel();
            checkTokenModel.setToken(oAuth2AccessToken.getValue());
            checkTokenModel.setExpiration(oAuth2AccessToken.getExpiration());
            checkTokenModel.setRefreshToken(oAuth2AccessToken.getRefreshToken().getValue());
            checkTokenModel.setActive(oAuth2AccessToken.getExpiresIn() > 0);
            Set<String> authorities = new HashSet<String>();
            for (GrantedAuthority authority : oAuth2Authentication.getAuthorities()) {
                authorities.add(authority.getAuthority());
            }
            checkTokenModel.setAuthorities(authorities);
            checkTokenModel.setResourceIds(oAuth2Authentication.getOAuth2Request().getResourceIds());
            checkTokenModel.setClientId(oAuth2Authentication.getOAuth2Request().getClientId());
            checkTokenModel.setUserName(oAuth2Authentication.getUserAuthentication().getName());
            return Result.ofSuccess(checkTokenModel);
        }
        return Result.ofFail("invalid token : " + tokenModel.getToken());
    }

    @Override
    public Result<OAuth2AccessToken> getToken(TokenModel tokenModel) {
        OAuth2AccessToken result = null;
        try {
            //手动校验clientId/secret是否匹配
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(tokenModel.getClientId());
            boolean matches = passwordEncoder.matches(tokenModel.getSecret(), clientDetails.getClientSecret());
            if (!matches) {
                return Result.ofFail("invalid_secret : " + tokenModel.getSecret());
            }
            UserDetails userDetails = userDetailService.loadUserByUsername(tokenModel.getClientId());
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("grant_type", tokenModel.getGrantType());
            parameters.put("username", tokenModel.getClientId());
            parameters.put("password", tokenModel.getSecret());
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(tokenModel.getClientId(), null, userDetails.getAuthorities());
            result = tokenEndPoint.postAccessToken(token, parameters).getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ofThrowable(e);
        }
        return Result.ofSuccess(result);
    }


}
