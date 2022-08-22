package com.lengao.oauth2.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * <p>description goes here</p>
 *
 * @author 冷澳
 * @date 2022/8/10
 */
@Configuration
public class BeanConfig {


    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new AuthorizationCodeServicesSon(dataSource);//设置授权码模式的授权码如何存取
    }

    @Bean
    public AuthorizationServerTokenServices tokenService() {
        DefaultTokenServices service = new DefaultTokenServices();
        service.setClientDetailsService(jdbcClientDetailsService(dataSource));
        service.setSupportRefreshToken(true);
        service.setTokenStore(tokenStore());
        return service;
    }

    @Bean()
    public ClientDetailsService jdbcClientDetailsService(DataSource dataSource) {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setPasswordEncoder(passwordEncoder());
        return clientDetailsService;
    }

    class AuthorizationCodeServicesSon extends JdbcAuthorizationCodeServices {

        private String deleteAuthenticationSql = "DELETE FROM oauth_code WHERE code = ? ";
        private String insertAuthenticationSql = "INSERT INTO oauth_code(code, client_id, authentication) values (?,?,?)";
        private String selectAuthenticationSql = "select client_id, code, authentication from oauth_code where code = ?";
        public AuthorizationCodeServicesSon(DataSource dataSource) {
            super(dataSource);
        }



        @Override
        public void setDeleteAuthenticationSql(String deleteAuthenticationSql) {
            this.deleteAuthenticationSql = "delete from oauth_code where code = ?";
        }

        @Override
        public void setInsertAuthenticationSql(String insertAuthenticationSql) {
            this.insertAuthenticationSql = "INSERT INTO oauth_code(code, client_id, authentication) values (?,?,?)";
        }

        @Override
        public void setSelectAuthenticationSql(String selectAuthenticationSql) {
            super.setSelectAuthenticationSql("select client_id, code, authentication from oauth_code where code = ?");
        }

        @Override
        public OAuth2Authentication consumeAuthorizationCode(String code) throws InvalidGrantException {
            OAuth2Authentication auth = this.remove(code);
            if (auth == null) {
                throw new InvalidGrantException("Invalid authorization code: " + code);
            } else {
                return auth;
            }
        }

        @Override
        public String createAuthorizationCode(OAuth2Authentication authentication) {
            return super.createAuthorizationCode(authentication);
        }

        @Override
        protected void store(String code, OAuth2Authentication authentication) {
            String clientId = authentication.getOAuth2Request().getClientId();
            jdbcTemplate.update(this.insertAuthenticationSql,code,clientId,authentication);
        }

//        @Override
//        public OAuth2Authentication remove(String code) {
//            String replace = this.selectAuthenticationSql.replace("?", "'"+code+"'");
//            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(replace);
//            String clientId = null;
//            if (sqlRowSet.next()) {
//                clientId  = sqlRowSet.getString("client_id");
//            }
//            jdbcTemplate.update(this.deleteAuthenticationSql,clientId);
//            return (OAuth2Authentication) jdbcTemplate.queryForObject(this.selectAuthenticationSql, new RowMapper<OAuth2Authentication>() {
//                public OAuth2Authentication mapRow(ResultSet rs, int rowNum) throws SQLException {
//                    return (OAuth2Authentication)SerializationUtils.deserialize(rs.getBytes("authentication"));
//                }
//            }, new Object[]{code});
//        }
    }
}
