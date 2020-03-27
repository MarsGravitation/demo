package com.microwu.cxd.manage.encode;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/26   15:35
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class PasswordEncodeTest {
    public static void test() {
        String password = "123456";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matches = passwordEncoder.matches(password, "$2a$10$LeV8rGAZTw3vmLfHn0KNXeY8ORbQZhueCX4NSf9EElzT.Ez7XyOs6");
        System.out.println(matches);
    }

    public static void main(String[] args) {
        test();
    }

}