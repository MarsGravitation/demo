package com.microwu.cxd.manage.security;

import com.microwu.cxd.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/20   10:47
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userService;

    @Autowired
    public WebSecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    CustomAuthenticationProvider customAuthenticationProvider() {
        CustomAuthenticationProvider authenticationProvider = new CustomAuthenticationProvider(userService);
        return authenticationProvider;
    }

    CustomJsonLoginFilter customJsonLoginFilter(AuthenticationManager authenticationManager) {
        CustomJsonLoginFilter customJsonLoginFilter = new CustomJsonLoginFilter();
        customJsonLoginFilter.setAuthenticationManager(authenticationManager);
        customJsonLoginFilter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
        customJsonLoginFilter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());
        return customJsonLoginFilter;
    }

    @Autowired
    private CustomSecurityConfig customSecurityConfig;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 这里必须要使用 formLogin 登录, 这样会使用我们的过滤器
     *
     * 以上的存在问题
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/22  14:04
     *
     * @param   	http
     * @return  void
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/register").permitAll()
                .antMatchers("/index").permitAll()
                .anyRequest().authenticated()
                .antMatchers("/user/**").hasAuthority("USER")
                .antMatchers("/admin/**").hasAuthority("ADMIN")
//                .and()
//                .formLogin()
//                .loginProcessingUrl("/login")
//                .permitAll()
                .and()
                .apply(customSecurityConfig)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                .clearAuthentication(true)
                .permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthneticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .csrf().disable();

        //在 UsernamePasswordAuthenticationFilter 的位置替换, 但是UsernamePasswordAuthenticationFilter还是会执行, 相当于before
//        http.addFilterAt(customJsonLoginFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);

    }

}