package com.microwu.cxd.controller;

import com.microwu.cxd.common.utils.JwtTokenUtil;
import com.microwu.cxd.common.utils.enums.JwtRedisEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/1/15   9:13
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
public class LogoutController {
    @Autowired
    private RedisTemplate redisTemplate;

    public static final Logger logger = LoggerFactory.getLogger(LogoutController.class);

    @GetMapping("/jwtLogout")
    public ResponseEntity logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        // 为嘛无需判断authHeader是否为null？ 因为Jwt过滤器已经判断过了。

        String authToken = authHeader.substring("Bearer ".length());
        String randomKey = JwtTokenUtil.getMd5KeyFromToken(authToken);
        String username = JwtTokenUtil.getUsernameFromToken(authToken);
        redisTemplate.delete(JwtRedisEnum.getTokenKey(username, randomKey));

        redisTemplate.delete(JwtRedisEnum.getAuthenticationKey(username, randomKey));

        logger.info("删除【{}】成功", JwtRedisEnum.getTokenKey(username, randomKey));
        logger.info("退出成功");

        return ResponseEntity.ok("退出成功");
    }
}