package com.microwu.cxd.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/20   11:55
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class HelloWorld {

    /**
     * 这里不要使用log4j2的API，使用slf4j 可以兼容其他的日志框架，但是我没有导包，这里先用 log4j2的API
     *
     * Logger Name：
     *  对应配置文件中的记录器名称
     *  如果缺省或者为null， 默认为该类的名称
     *  例如在此类中，以下案例效果是一样的
     *  LogManager.getLogger(HelloWorld.class);
     *  LogManager.getLogger(HelloWorld.class.getName());
     *  LogManager.getLogger();
     */
    private static final Logger logger = LogManager.getLogger(HelloWorld.class);

    public static void main(String[] args) {
        // hello world
        logger.info("hello world!");

        // 替代参数
        logger.info("用户 {} 与 生日 {}", "成旭东", "1996-10-17");

        // 对于异常的处理，对于SpringBoot项目不支持
        try {
            throw new RuntimeException("运行时异常！！！");
        } catch (Exception e) {
            logger.error("程序发生异常", e);
        }
    }

}