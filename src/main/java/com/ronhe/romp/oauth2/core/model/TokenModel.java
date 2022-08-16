package com.ronhe.romp.oauth2.core.model;


import java.util.Date;
import java.util.Set;

/**
 * <p>description goes here</p>
 *
 * @author 冷澳
 * @date 2022/8/14
 */
public class TokenModel {
    /**
     * token值
     */
    String token;

    /**
     * 用户名
     */
    String userName;

    /**
     * 密码
     */
    String passWord;

    /**
     * 客户端Id
     */
    String clientId;

    String secret;

    String grantType;
    /**
     * token是否有效
     */
    Boolean active;
    /**
     * token过期时间
     */
    Date expiration;
    /**
     * 刷新token值
     */
    String refreshToken;

    /**
     * 可访问资源id集合
     */
    Set<String> resourceIds;

    /**
     * 可访问权限集合
     */
    Set<String> authorities;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Set<String> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(Set<String> resourceIds) {
        this.resourceIds = resourceIds;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }
}
