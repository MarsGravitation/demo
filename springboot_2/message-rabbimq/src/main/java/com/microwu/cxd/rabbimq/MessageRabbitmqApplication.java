package com.microwu.cxd.rabbimq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/12   15:54
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SpringBootApplication
public class MessageRabbitmqApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageRabbitmqApplication.class, args).close();
    }
}