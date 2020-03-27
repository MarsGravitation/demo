package com.microwu.cxd.manage.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microwu.cxd.manage.domain.MobileAndVerify;
import com.microwu.cxd.manage.token.SmsCodeAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/19   15:21
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private String mobileParameter = "mobile";

    private boolean postOnly = true;

    public SmsCodeAuthenticationFilter() {
        // 处理的手机验证码登录的请求URL
        super(new AntPathRequestMatcher("/authentication/mobile", "POST"));
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        // 1. 判断是不是 post 请求
        if(postOnly && !httpServletRequest.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported !");
        }
        // 2. 从请求中获取手机号码
        ObjectMapper objectMapper = new ObjectMapper();
        MobileAndVerify mobileAndVerify = null;
        mobileAndVerify = objectMapper.readValue(httpServletRequest.getInputStream(), MobileAndVerify.class);
        String mobile = mobileAndVerify.getMobile();
        if(StringUtils.isEmpty(mobile)) {
            throw new AuthenticationServiceException("mobile is null !");
        }
        SmsCodeAuthenticationToken authenticationToken = new SmsCodeAuthenticationToken(mobile);
        // 3. 交给manager 进行校验
        return super.getAuthenticationManager().authenticate(authenticationToken);
    }
}