package com.microwu.cxd.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   9:36
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SpringBootApplication
public class CimServerApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(CimServerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CimServerApplication.class, args);
        LOGGER.info("cim server application start !!!");
    }

}