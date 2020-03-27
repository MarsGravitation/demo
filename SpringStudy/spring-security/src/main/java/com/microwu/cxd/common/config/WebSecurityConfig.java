package com.microwu.cxd.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/6/27   11:25
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * WebSecurityConfig 和 EnableWebSecurity, 使用SpringSecurity的网络安全支持,
     * 并提供了SpringMVC的整合. 它还扩展了WebSecurityConfigurerAdapter并覆盖了其几种方法,
     * 已设置Web安全配置的一些细节
     *
     * 该configure方法定义了那些URL路径应该被保护, 那些不应该保护. 具体而言, / 和 /home路径配置
     * 为不需要任何身份验证. 必须对所有其他路径进行身份验证.
     *
     * 当用户成功登陆时, 它们将被重定向到先前请求的身份验证页面. 有一个自定义的 /login页面, 每个人都可以查看它
     *
     * 至于该userDetailsService(), 它使用单个用户设置内存用户存储. 该用户被赋予用户名"user", 密码"password", 角色为"USER"
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/6/27  11:35
     *
     * @param   	http
     * @return  void
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/home").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
}