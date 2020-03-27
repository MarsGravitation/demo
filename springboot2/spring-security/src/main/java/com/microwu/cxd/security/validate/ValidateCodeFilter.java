package com.microwu.cxd.security.validate;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/1/14   13:36
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = httpServletRequest.getRequestURI();
        if("/login".equals(requestURI)) {
            String code = redisTemplate.opsForValue().get("image");
            if(StringUtils.isEmpty(code)) {
                return;
            } else {
                String validate = httpServletRequest.getParameter("code");
                if(StringUtils.isEmpty(validate)) {
                    return;
                } else if(!validate.equals(code)) {
                    return;
                }
            }
            redisTemplate.delete("image");
        } else if("/mobile".equals(requestURI)) {
            String code = redisTemplate.opsForValue().get("mobile");
            if(StringUtils.isEmpty(code)) {
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }

}