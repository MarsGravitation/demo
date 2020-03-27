package com.microwu.cxd.config;

import com.microwu.cxd.security.json.JsonSecurityConfig;
import com.microwu.cxd.security.jwt.JwtSecurityConfig;
import com.microwu.cxd.security.sms.SmsSecurityConfig;
import com.microwu.cxd.security.validate.ValidateSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/1/14   9:09
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private JsonSecurityConfig jsonSecurityConfig;

    @Autowired
    private JwtSecurityConfig jwtSecurityConfig;

    @Autowired
    private ValidateSecurityConfig validateSecurityConfig;

    @Autowired
    private SmsSecurityConfig smsSecurityConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/register").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers( "/sms", "/image").permitAll()
                .anyRequest().authenticated()
                .antMatchers("/user/**").hasAuthority("USER")
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .and()
                .apply(validateSecurityConfig)
                .and()
                .apply(jsonSecurityConfig)
                .and()
                .apply(jwtSecurityConfig)
                .and()
                .apply(smsSecurityConfig)
                .and()
                .exceptionHandling()
                .and()
                .csrf().disable();

        http.authorizeRequests().anyRequest().authenticated();
    }
}