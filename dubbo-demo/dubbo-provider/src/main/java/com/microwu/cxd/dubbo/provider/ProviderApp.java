package com.microwu.cxd.dubbo.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/21   10:15
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ProviderApp {
    public static void main(String[] args) throws IOException {
        // 加载xml 配置文件启动
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/provider.xml");
        context.start();
        // 按任意键退出
        System.in.read();
    }
}