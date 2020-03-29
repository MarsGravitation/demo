package com.microwu.cxd.demo.scheduler;

import com.microwu.cxd.demo.job.HelloJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * Description:
 * Author:   Administration
 * Date:     2019/3/18 14:57
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class SecondeScheduler {
    public static void main(String[] args) throws SchedulerException {
        // 1.创建要给JobDetail实例，将该实例与HelloJob绑定
        JobDetail jobDetail = JobBuilder.newJob(HelloJob.class)
                .withIdentity("zhlJob").build();
        // 开始时间 3秒钟之后
        Date sDate = new Date();
        sDate.setTime(sDate.getTime() + 3000);
        // 结束时间
        Date eDate = new Date();
        eDate.setTime(eDate.getTime() + 6000);
        // 2.创建一个Trigger实例，定义该job3秒后执行，在6秒之后结束
        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("zhlTrigger")
                .startAt(sDate).endAt(eDate).
                        withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2).repeatForever())
                .build();
        // 创建Scheduler实例
        StdSchedulerFactory factory = new StdSchedulerFactory();
        Scheduler scheduler = factory.getScheduler();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();

    }
}