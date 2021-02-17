package com.microwu.cxd.server.config;

import com.microwu.cxd.common.constant.Constants;
import com.microwu.cxd.common.protocol.CIMRequestProto;
import com.microwu.cxd.server.properties.CimServerProperties;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   11:15
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration(proxyBeanMethods = false)
public class CimServerConfig {

    @Autowired
    private CimServerProperties properties;

    @Bean
    public ZooKeeper zooKeeper() throws IOException {
        return new ZooKeeper(properties.getConnectString(), properties.getSessionTimeout(), null);
    }

    @Bean
    public CIMRequestProto.CIMReqProtocol heartBeat() {
        return CIMRequestProto.CIMReqProtocol.newBuilder()
                .setRequestId(0L)
                .setReqMsg("pong")
                .setType(Constants.CommandType.PING)
                .build();
    }

}