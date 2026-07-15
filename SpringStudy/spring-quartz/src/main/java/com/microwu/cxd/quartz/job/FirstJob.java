package com.microwu.cxd.quartz.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/19   10:20
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class FirstJob extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirstJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String now = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        LOGGER.info("first job now: {}", now);
    }
}
