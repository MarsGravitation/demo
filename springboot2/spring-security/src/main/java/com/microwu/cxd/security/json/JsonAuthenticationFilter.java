package com.microwu.cxd.security.json;

import com.microwu.cxd.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/1/14   9:06
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        if (MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())
//                || MediaType.APPLICATION_JSON_UTF8_VALUE.equals(request.getContentType())) {
//
//        } else {
//            return super.attemptAuthentication(request, response);
//        }
        JsonAuthenticationToken token = null;
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        token = new JsonAuthenticationToken(user);
        return this.getAuthenticationManager().authenticate(token);
    }
}