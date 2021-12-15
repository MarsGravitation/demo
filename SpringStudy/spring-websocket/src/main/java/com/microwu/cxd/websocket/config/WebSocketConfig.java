package com.microwu.cxd.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 开启 WebSocket 支持
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/5  16:30
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
@Configuration
public class WebSocketConfig {

    /**
     *
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/5     16:31
     *
     * @param
     * @return org.springframework.web.socket.server.standard.ServerEndpointExporter
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
