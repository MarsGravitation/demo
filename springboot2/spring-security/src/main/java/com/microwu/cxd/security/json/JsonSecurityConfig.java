package com.microwu.cxd.security.json;

import com.microwu.cxd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/1/14   9:28
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class JsonSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private JsonAuthenticationSuccessHandler jsonAuthenticationSuccessHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JsonAuthenticationFilter jsonAuthenticationFilter = new JsonAuthenticationFilter();
        jsonAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        JsonAuthenticationProvider jsonAuthenticationProvider = new JsonAuthenticationProvider(userService, passwordEncoder);
        jsonAuthenticationFilter.setAuthenticationSuccessHandler(jsonAuthenticationSuccessHandler);
        http
                .authenticationProvider(jsonAuthenticationProvider)
                .addFilterAt(jsonAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}