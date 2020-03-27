package com.microwu.cxd.manage.config;

import com.microwu.cxd.manage.filter.IpAuthenticationProcessFilter;
import com.microwu.cxd.manage.provider.IpAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Description:     WebSecurityAdapter 提供了我们很大的便利, 不需要关注AuthenticationManager 什么时候创建, 只需要使用其暴露的
 *      configue 便可以添加我们自定义的provider
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/19   11:17
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    IpAuthenticationProvider ipAuthenticationProvider() {
         return new IpAuthenticationProvider();
    }

    /**
     * 配置封装ipAuthenticationToken的过滤器
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/19  11:18
     *
     * @param   	authenticationManager
     * @return  IpAuthenticationProcessFilter
     */
    IpAuthenticationProcessFilter ipAuthenticationProcessFilter(AuthenticationManager authenticationManager) {
        IpAuthenticationProcessFilter ipAuthenticationProcessFilter = new IpAuthenticationProcessFilter();
        // 为过滤器添加认证器
        ipAuthenticationProcessFilter.setAuthenticationManager(authenticationManager);
        // 重写认证失败时跳转的页面
        ipAuthenticationProcessFilter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/ipLogin?error"));
        return ipAuthenticationProcessFilter;
    }

    @Bean
    LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint() {
        // 配置登录端点
        LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint = new LoginUrlAuthenticationEntryPoint("/ipLogin");
        return loginUrlAuthenticationEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/home").permitAll()
                .antMatchers("/ipLogin").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/ipLogin")
                .authenticationEntryPoint(loginUrlAuthenticationEntryPoint());

        // 注册filter, 注意放置的顺序
        http.authenticationProvider(ipAuthenticationProvider()).addFilterBefore(ipAuthenticationProcessFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(ipAuthenticationProvider());
//    }
}