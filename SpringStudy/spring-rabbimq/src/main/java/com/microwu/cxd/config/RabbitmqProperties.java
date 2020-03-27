package com.microwu.cxd.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/31   17:16
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Component
@Data
public class RabbitmqProperties {
    private String host;

    private Integer port;

    private String username;

    private String password;
}