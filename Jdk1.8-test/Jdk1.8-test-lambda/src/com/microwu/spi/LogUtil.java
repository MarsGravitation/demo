package com.microwu.spi;

import java.util.ServiceLoader;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2025/3/25   13:50
 * Update History:
 * Author        Time            Content
 */
public class LogUtil {

    private static final Log logger;

    static {
        ServiceLoader<Log> logLoader = ServiceLoader.load(Log.class);
        logger = logLoader.iterator().next();
    }

    public static void log(String info) {
        logger.log(info);
    }

}
