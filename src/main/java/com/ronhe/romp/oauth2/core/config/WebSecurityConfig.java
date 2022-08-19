package com.ronhe.romp.oauth2.core.config;

import com.ronhe.romp.oauth2.core.filter.AuthenticationTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * <p>description goes here</p>
 *
 * @author 冷澳
 * @date 2022/8/10
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 用于支持password模式
     */
    @Bean //防止服务器@Autowired authenticationManager无法注入
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//禁止生成cookie
//                .and().addFilterBefore(new AuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);//免登录过滤器
//    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//禁止生成cookie
                .and().addFilterBefore(new AuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)//免登录过滤器
                .authorizeRequests().antMatchers("/oauth/**").permitAll()
                .and().formLogin().permitAll()
                .and().csrf().disable().httpBasic();
    }
}
