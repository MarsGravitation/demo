package com.microwu.cxd.dynamic.proxy;

import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Description: JDK动态代理的测试类
 * Author:   Administration
 * Date:     2019/2/27 13:52
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class ProxyTest {
    public static void main(String[] args) {
        // 被代理对象
        UserService userService = new UserServiceImpl();
        // 实例化InvocationHandler
        MyInvocationHandler invocationHandler = new MyInvocationHandler(userService);
        // 获取代理对象
        UserService proxy = (UserService)invocationHandler.getProxy();
        // 执行方法
        proxy.execute();
        // 生成代理类class文件
//        try {
//            createProxyFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static void createProxyFile() throws IOException {
        byte[] generateProxyClass = ProxyGenerator.generateProxyClass("$Proxy0", new Class<?>[]{UserService.class});
        FileOutputStream outputStream = new FileOutputStream("$Proxy0.class");
        outputStream.write(generateProxyClass);
        outputStream.close();
    }
}