package com.microwu.cxd.dubbo.provider;

import com.microwu.cxd.dubbo.service.EchoService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/28   13:41
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@EnableDiscoveryClient
@EnableAutoConfiguration
public class DubboServerBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(DubboServerBootstrap.class, args);
    }

}

@Service
class EchoServiceImpl implements EchoService {

    @Override
    public String echo(String message) {
        return "[echo] Hello, " + message;
    }
}