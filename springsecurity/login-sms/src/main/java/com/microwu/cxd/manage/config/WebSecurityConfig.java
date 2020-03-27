package com.microwu.cxd.manage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/26   17:20
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SmsCodeAuthenticationSecurityconfig smsCodeAuthenticationSecurityconfig;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
        http
                .apply(validateCodeSecurityConfig)//验证码拦截
                .and()
                .apply(smsCodeAuthenticationSecurityconfig)
                .and()
                .authorizeRequests()
                .antMatchers("/generate/code").permitAll()
                .anyRequest()
                .authenticated()
                .antMatchers("/index").hasAuthority("USER")
                .and()
                .csrf().disable();
    }
}