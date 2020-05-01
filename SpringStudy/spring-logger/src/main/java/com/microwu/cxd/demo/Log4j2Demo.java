package com.microwu.cxd.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/20   9:59
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Log4j2Demo {

    /**
     * 定义一个静态logger，让他指向 Logger 实例
     */
    private static final Logger logger = LogManager.getLogger(Log4j2Demo.class);

    /**
     * 如果Log4j找不到配置文件，它将提供默认配置。DefaultConfiguration 的默认配置：
     *  1. ConsoleAppender 附着到 root logger
     *  2. PatternLayout 设置模式 - "%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"
     *  注意：默认的配置 分配给 root logger 的 ERROR
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/20  10:20
     *
     * @param   	args
     * @return  void
     */
    public static void main(String[] args) {

        logger.trace("Entering application.");
        Bar bar = new Bar();
        if (!bar.doIt()) {
            logger.error("Didn't do it.");
        }
        logger.trace("Exiting application.");

    }
}