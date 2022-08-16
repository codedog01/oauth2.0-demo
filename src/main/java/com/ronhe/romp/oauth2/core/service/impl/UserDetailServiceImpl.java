package com.ronhe.romp.oauth2.core.service.impl;

import com.ronhe.romp.oauth2.core.mapper.TbUserMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>description goes here</p>
 *
 * @author 冷澳
 * @date 2022/8/10
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TbUserMapper tbUserMapper;

    //重写认证的过程，由AuthenticationManager去调，从数据库中查找用户信息,每次获取token时触发
    @Override
    public UserDetails loadUserByUsername(String clientId) throws UsernameNotFoundException {
        //查询用户,这里查的是oauth_client_details表，clientId作为用户名secret作为密码
        String secret = tbUserMapper.findByClientId(clientId);
        if (StringUtils.isEmpty(secret)) {
            throw new RuntimeException("client_id: " + clientId + " can not be found");
        }
        //查询用户权限
        List<GrantedAuthority> perms = new ArrayList<>();
        List<String> serviceUrlList = tbUserMapper.findPermissionByClientId(clientId);
        for (String url : serviceUrlList) {
            perms.add(new SimpleGrantedAuthority(url));
        }
        return  new User(clientId, secret, perms);
    }
}

