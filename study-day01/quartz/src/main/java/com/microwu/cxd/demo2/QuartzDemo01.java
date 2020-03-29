package com.microwu.cxd.demo2;

import com.microwu.cxd.demo.job.HelloJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/3/28   17:11
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class QuartzDemo01 {

    public static void main(String[] args) throws SchedulerException {
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        // 通过工厂获取调度器
        Scheduler scheduler = stdSchedulerFactory.getScheduler();

        // 启动调度器
        scheduler.start();

        // 定义一个Job
        JobDetail jobDetail = JobBuilder.newJob(HelloJob.class).withIdentity("myJob", "group1").build();

        // 现在开始，每40秒 调度一次 job
        SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger().withIdentity("myTrigger", "group1").startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(40).repeatForever()).build();

        // 告诉 quartz 使用 simpleTrigger 调度任务
        scheduler.scheduleJob(jobDetail, simpleTrigger);
    }
}