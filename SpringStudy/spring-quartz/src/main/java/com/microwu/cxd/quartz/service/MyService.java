package com.microwu.cxd.quartz.service;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/4  11:19
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
@Service
public class MyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyService.class);

    public void someMethod(JobExecutionContext context, String name) {
        Object msg = context.getJobDetail().getJobDataMap().get("msg");
        LOGGER.info("time = {}, msg = {}, name = {}", LocalDateTime.now(), msg, name);
    }

}
