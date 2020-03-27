package com.microwu.cxd.manage.config;

import com.alibaba.fastjson.JSON;
import com.microwu.cxd.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/23   17:55
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Bean
    public JsonAuthenticationProvider authenticationProvider() {
        JsonAuthenticationProvider jsonAuthenticationProvider = new JsonAuthenticationProvider();
        jsonAuthenticationProvider.setUserService(userService);
        return jsonAuthenticationProvider;
    }

    public JsonAuthenticationFilter jsonAuthenticationFilter() throws Exception {
        JsonAuthenticationFilter authenticationFilter = new JsonAuthenticationFilter();
        AuthenticationManager authenticationManager = super.authenticationManager();
        authenticationFilter.setAuthenticationManager(authenticationManager);
        authenticationFilter.setAuthenticationSuccessHandler(((httpServletRequest, httpServletResponse, authentication) -> {
            httpServletResponse.getWriter().write(JSON.toJSONString(authentication));
        }));
        authenticationFilter.setAuthenticationFailureHandler((httpServletRequest, httpServletResponse, e) -> {
            httpServletResponse.getWriter().write(JSON.toJSONString("fail authentication"));
        });
        return authenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/user/**").hasAuthority("USER")
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(((httpServletRequest, httpServletResponse, e) -> {
                    httpServletResponse.getWriter().write(JSON.toJSONString("no login"));
                }))
                .accessDeniedHandler((httpServletRequest, httpServletResponse, e) -> {
                    httpServletResponse.getWriter().write(JSON.toJSONString("no authentication"));
                })
                .and()
                .csrf().disable();

        //在 UsernamePasswordAuthenticationFilter 的位置替换, 但是UsernamePasswordAuthenticationFilter还是会执行, 相当于before
        http.authenticationProvider(authenticationProvider()).addFilterAt(jsonAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}