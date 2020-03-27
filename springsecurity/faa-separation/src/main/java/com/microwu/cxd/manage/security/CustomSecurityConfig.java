package com.microwu.cxd.manage.security;

import com.microwu.cxd.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/1/13   18:48
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class CustomSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private UserService userService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        CustomJsonLoginFilter customJsonLoginFilter = new CustomJsonLoginFilter();
        customJsonLoginFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        customJsonLoginFilter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
        CustomAuthenticationProvider customAuthenticationProvider = new CustomAuthenticationProvider(userService);
        http.authenticationProvider(customAuthenticationProvider).addFilterAt(customJsonLoginFilter, UsernamePasswordAuthenticationFilter.class);
    }
}