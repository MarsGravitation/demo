package com.microwu.cxd.spring.properties;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/1   15:06
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BootStrap {
    public static void main(String[] args) {
        ClassPathPropertyFileApplicationContext context = new ClassPathPropertyFileApplicationContext("");
        context.getBeansOfType(null);
    }
}