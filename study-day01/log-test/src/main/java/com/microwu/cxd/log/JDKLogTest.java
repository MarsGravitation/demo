package com.microwu.cxd.log;

import java.util.logging.*;

/**
 * Description: JDK的日志测试类
 * Author:   Administration
 * Date:     2019/3/11 11:33
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class JDKLogTest {
    public static void main(String[] args) {
        // 获取logger对象，logger有三个重要的概念---Handler、Formatter、Level
        Logger logger = Logger.getLogger("logger");
        logger.info("Hello World!");

        // Handler控制日志的输出
        Handler handler = new Handler() {
            @Override
            public void publish(LogRecord record) { }
            @Override
            public void flush() { }
            @Override
            public void close() throws SecurityException { }
        };

        // Formatter控制日志的格式
        handler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return null;
            }
        });

        // Level控制日志的级别，高级别会忽略低级别的
        logger.setLevel(Level.INFO);
        logger.log(Level.FINEST, "Hello World");
    }
}