package com.microwu.cxd.manage.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microwu.cxd.manage.domain.UserVO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/23   17:52
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        UserVO userVO = null;
        try {
            userVO = objectMapper.readValue(request.getInputStream(), UserVO.class);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return super.getAuthenticationManager().authenticate(new JsonAuthenticationToken(userVO));
        }
    }
}