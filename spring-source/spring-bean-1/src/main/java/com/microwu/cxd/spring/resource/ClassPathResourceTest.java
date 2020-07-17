package com.microwu.cxd.spring.resource;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * Description: 使用 spring 提供的获取类路径下的资源
 *  - 底层还是通过 classloader 获取的
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/3   15:41
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ClassPathResourceTest {
    public static void main(String[] args) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("a.txt");
        System.out.println(classPathResource.getInputStream());
    }
}