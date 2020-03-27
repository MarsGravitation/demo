package com.microwu.cxd.manage.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microwu.cxd.manage.domain.UserVO;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/26   9:47
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CustomAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    protected CustomAuthenticationProcessingFilter() {
        super(new AntPathRequestMatcher("/ajaxLogin"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //attempt Authentication when Content-Type is json
        if(request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)
                ||request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)){

            //use jackson to deserialize json
            ObjectMapper mapper = new ObjectMapper();
            CustomAuthenticationToken authRequest = null;
            try (InputStream is = request.getInputStream()){
                UserVO userVO = mapper.readValue(is,UserVO.class);
                authRequest = new CustomAuthenticationToken(userVO);
            }catch (IOException e) {
                e.printStackTrace();
                authRequest = new CustomAuthenticationToken(null);
            }finally {
                return this.getAuthenticationManager().authenticate(authRequest);
            }
        }
        return null;
    }
}