package com.microwu.cxd.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/1/14   14:54
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
public class ValidateController {

    public static final Logger logger = LoggerFactory.getLogger(ValidateController.class);

    @Autowired
    private RedisTemplate redisTemplate;

    private String source = "23456789ABCDEFGHJKMNPQRSTUVWXYZ";

    @GetMapping("/image")
    public void createImage(HttpServletRequest request, HttpServletResponse response) {
        String generate = generate(4, source);
        logger.info("图形验证码 {}", generate);
        redisTemplate.opsForValue().set("image", generate);
    }

    @GetMapping("/sms")
    public void createSms(String mobile) {
        String generate = generate(4, source);
        logger.info("短信验证码 {}", generate);
        redisTemplate.opsForValue().set("mobile", generate);
    }

    public static String generate(int verifySize, String sources) {
        int codesLen = sources.length();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder verifyCode = new StringBuilder(verifySize);
        for (int i = 0; i < verifySize; i++) {
            verifyCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
        }
        return verifyCode.toString();
    }
}