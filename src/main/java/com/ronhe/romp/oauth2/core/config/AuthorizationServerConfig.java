package com.ronhe.romp.oauth2.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * <p>description goes here</p>
 *
 * @author 冷澳
 * @date 2022/8/10
 */

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    @Qualifier("userDetailServiceImpl")
    public UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    AuthorizationServerTokenServices tokenService;

    @Autowired
    private ClientDetailsService jdbcClientDetailsService;


//    /**
//     * @Description: 配置 token 节点的安全策略
//     */
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security.tokenKeyAccess("de()")
//                .checkTokenAccess("permitAll()")//默认"denyAll()"，不允许访问/oauth/check_token；"isAuthenticated()"需要携带auth；"permitAll()"直接访问
//                .allowFormAuthenticationForClients();                //表单认证（申请令牌）
//    }

    /**
     * @Description: 配置客户端信息, 相当于在认证服务器中注册哪些客户端（包括资源服务器）能访问
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService);

    }

    /**
     * @Description: 配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager) // 开启密码验证
                .tokenStore(tokenStore) // 设置tokenStore，生成token时会向数据库中保存
                .tokenServices(tokenService)//令牌管理服务
                .userDetailsService(userDetailsService);
    }
}
