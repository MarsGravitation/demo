package com.microwu.cxd.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * Author:   Administration
 * Date:     2019/3/11 11:56
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class SLF4JTest {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(SLF4JTest.class);
        logger.error("Hello World");
    }
}