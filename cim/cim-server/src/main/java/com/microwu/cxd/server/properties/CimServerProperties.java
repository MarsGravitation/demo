package com.microwu.cxd.server.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   10:34
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cim.server")
public class CimServerProperties {

    private String connectString;

    private int sessionTimeout;

    private String zkRoot;

    private long heartBeatTime;

}