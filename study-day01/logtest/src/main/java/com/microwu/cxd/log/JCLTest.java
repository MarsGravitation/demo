package com.microwu.cxd.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Description: commons-logging是一个统一操作的门面，不涉及具体的功能
 * Author:   Administration
 * Date:     2019/3/11 11:48
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class JCLTest {
    private static Log logger = LogFactory.getLog(JCLTest.class);

    public static void main(String[] args) {
        logger.info("Hello World!");
    }
}