package com.microwu.cxd.manage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Description:     注解: @EnableWebSecurity, 该注解和@Configuration 注解一起使用, 注解WebSecurityConfigurer类
 *                                      或者 继承WebSecurityConfigurerAdapter + @EnableWebSecurity()
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/16   14:06
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * "/" 不需要权限即可访问
     * "/user" 及其所有路径, 都需要"User"权限
     * "/login" 登录成功默认跳转页面 /user
     * 退出登录地址 /logout, 退出登录跳转页面 /login
     * 默认启用 CSRF
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/16  14:06
     *
     * @param   	http
     * @return  void
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/map/**").permitAll()
                .antMatchers("/user/**").hasRole("USER")
                .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/user")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login");
    }

    /**
     * 在内存中创建一个名为"user"的用户
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/16  14:10
     *
     * @param
     * @return  org.springframework.security.core.userdetails.UserDetailsService
     */
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        userDetailsManager.createUser(userBuilder.username("cxd").password("123456").roles("USER").build());
        return userDetailsManager;
    }
}