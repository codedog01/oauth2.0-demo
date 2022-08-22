package com.ronhe.romp.oauth2.core;

import com.ronhe.romp.oauth2.core.constant.Constants;
import com.ronhe.romp.oauth2.core.mapper.TbUserMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>description goes here</p>
 *
 * @author 冷澳
 * @date 2022/8/14
 */
@RestController
@RequestMapping("/oauth")
public class NewCheckTokenEndpoint {
    /**
     * token
     */
    String TOKEN = "token";

    /**
     * 服务名/服务id
     */
    String SERVER_NAME = "server_name";

    /**
     * 子路径
     */
    String SUB_PATH = "sub_path";

    @Autowired
    TokenStore tokenStore;

    @Autowired
    AuthorizationServerTokenServices tokenService;

    @Autowired
    CheckTokenEndpoint checkTokenEndpoint;

    @Autowired
    ClientDetailsService clientDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ClientDetailsService jdbcClientDetailsService;

    @Autowired
    AuthorizationEndpoint authorizationEndpoint;

    @Autowired
    AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    TbUserMapper mapper;

    /**
     * @参数示例 {"token": "a792b89e-7010-4f3a-9567-a55ff29cc4c8", server_name": "admin",sub_path": "/test"}
     */
    @PostMapping("/checkToken")

    public Map<String, Object> checkToken(@RequestBody Map<String, String> parameters) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        if (StringUtils.isEmpty(parameters.get(SERVER_NAME)) || StringUtils.isEmpty(parameters.get(SUB_PATH))) {
            response.put(Constants.ERROR, "illegal parameter");
            response.put(Constants.ERROR_DESCRIPTION, "Please check the parameters :" + SERVER_NAME + "," + SUB_PATH + "," + TOKEN);
            return response;
        }

        OAuth2Authentication oAuth2Authentication;
        checkTokenEndpoint.checkToken(TOKEN);
        oAuth2Authentication = tokenStore.readAuthentication(parameters.get(TOKEN));
        for (GrantedAuthority grantedAuthority : oAuth2Authentication.getAuthorities()) {
            String authority = grantedAuthority.getAuthority();
            int index = authority.indexOf("/**");
            if (index != -1 && authority.substring(0, index).equals(parameters.get(SERVER_NAME))) {
                response.put(Constants.SUCCESS, true);
                return response;
            } else {
                if (parameters.get(SERVER_NAME).concat(parameters.get(SUB_PATH)).equals(authority)) {
                    response.put(Constants.SUCCESS, true);
                    return response;
                }
            }
        }
        response.put(Constants.ERROR, "403");
        response.put(Constants.ERROR_DESCRIPTION, "token has no access");
        return response;
    }

    /**
     * @参数示例 {"token": "a792b89e-7010-4f3a-9567-a55ff29cc4c8", server_name": "admin",sub_path": "/test"}
     */
    @GetMapping("/removeToken")
    public Map<String, Object> removeToken(@RequestBody Map<String, String> parameters) {
        String clientId = parameters.get("client_id");
        String secret = parameters.get("client_secret");
        String token = parameters.get("token");
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        if (StringUtils.isEmpty(clientId) || StringUtils.isEmpty(secret) || StringUtils.isEmpty(token)) {
            throw OAuth2Exception.create("illegal parameter", "client_id client_secret token Can not be empty");
        }
        boolean matches = passwordEncoder.matches(secret, clientDetails.getClientSecret());
        if (matches) {
            tokenStore.removeAccessToken(tokenStore.readAccessToken(token));
        } else {
            throw OAuth2Exception.create("Authentication is invalid", "invalid client_id/client_secret");
        }
        Map<String, Object> res = new LinkedHashMap<>();
        res.put(Constants.SUCCESS, true);
        return res;
    }

    /**
     * @参数示例 {"token": "a792b89e-7010-4f3a-9567-a55ff29cc4c8", server_name": "admin",sub_path": "/test"}
     */
    @GetMapping("/getCode")
    public String getCode(String client_id) {
        List<String> codeByClientId = mapper.findCodeByClientId(client_id);
        if (!codeByClientId.isEmpty()) {
            return codeByClientId.get(0);
        } else return null;
    }

}
