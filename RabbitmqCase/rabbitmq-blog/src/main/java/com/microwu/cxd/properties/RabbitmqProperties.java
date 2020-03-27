package com.microwu.cxd.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/14   19:59
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
@Component
@ConfigurationProperties("spring.rabbitmq")
public class RabbitmqProperties {
    private String host;

    private Integer port;

    private String username;

    private String password;
}