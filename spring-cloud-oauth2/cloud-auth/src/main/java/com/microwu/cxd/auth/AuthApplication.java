package com.microwu.cxd.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Description: 实现授权和资源服务器分离
 * https://gitee.com/niugangxy/springcloud/tree/master/spring-cloud-learn-code/929.spring-cloud-oauth2
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/7   17:45
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SpringBootApplication
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}