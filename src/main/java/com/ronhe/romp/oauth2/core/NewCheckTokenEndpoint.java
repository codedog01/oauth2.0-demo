package com.ronhe.romp.oauth2.core;

import com.ronhe.romp.oauth2.core.constant.Constants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
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
    CheckTokenEndpoint checkTokenEndpoint;

    /**
     * header: Authorization:bearer token
     * 所需参数 token
     *
     * @return
     */
    @PostMapping("/checkToken")
    public Map<String, Object> checkToken(@RequestBody Map<String, String> parameters) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        if (StringUtils.isEmpty(parameters.get(SERVER_NAME)) || StringUtils.isEmpty(parameters.get(SUB_PATH))) {
            response.put(Constants.ERROR, "illegal parameter");
            response.put(Constants.ERROR_DESCRIPTION, "Please check the parameters :"+ SERVER_NAME+","+SUB_PATH+","+TOKEN);
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

}
