package com.microwu.cxd.security.json;

import com.alibaba.fastjson.JSON;
import com.microwu.cxd.common.utils.JwtTokenUtil;
import com.microwu.cxd.common.utils.enums.JwtRedisEnum;
import com.microwu.cxd.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/1/14   9:29
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
//        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
//        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.toString());
//        httpServletResponse.getWriter().write(JSON.toJSONString(authentication));

        final String randomKey = JwtTokenUtil.getRandomKey();
        String username = ((User) authentication.getPrincipal()).getUsername();

        final String token = JwtTokenUtil.generateToken(username, randomKey);

        // 存到redis
        redisTemplate.opsForValue().set(JwtRedisEnum.getTokenKey(username, randomKey),
                token,
                300,
                TimeUnit.SECONDS);

        // 将认证信息存到redis，方便后期业务用，也方便 JwtAuthenticationTokenFilter用
        redisTemplate.opsForValue().set(JwtRedisEnum.getAuthenticationKey(username, randomKey),
                JSON.toJSONString(authentication),
                300,
                TimeUnit.SECONDS
        );
        httpServletResponse.setHeader("Authorization", "Bearer " + token);

        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(authentication));

    }
}