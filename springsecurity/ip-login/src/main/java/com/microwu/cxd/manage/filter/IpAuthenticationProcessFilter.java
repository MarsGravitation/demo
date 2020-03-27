package com.microwu.cxd.manage.filter;

import com.microwu.cxd.manage.token.IpAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/18   15:51
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class IpAuthenticationProcessFilter extends AbstractAuthenticationProcessingFilter {

    public IpAuthenticationProcessFilter() {
        super(new AntPathRequestMatcher("/ipVerify"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String remoteHost = httpServletRequest.getRemoteHost();
        // 实现解耦, 交给内部的Manager认证
        return super.getAuthenticationManager().authenticate(new IpAuthenticationToken(remoteHost));
    }
}