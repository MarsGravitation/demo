package com.microwu.cxd.demo.scheduler;

import com.microwu.cxd.demo.job.HelloJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Description:
 *  1.创建调度工厂 --- 工厂模式
 *  2.根据工厂取得调度器实例 --- 工厂模式
 *  3.Builder模式构建子组件<Job, Trigger> --- builder模式
 *  4.通过调度器组装子组件 --- 工厂模式
 *  5.调度器.start() --- 工厂模式
 * Author:   Administration
 * Date:     2019/3/18 11:28
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class HelloScheduler {
    public static void main(String[] args) throws SchedulerException {
        // 创建一个jobDetail的实例，将该实例与HelloJob Class绑定
        JobDetail jobDetail = JobBuilder.newJob(HelloJob.class).withIdentity("MyJob").build();
        // 创建一个Trigger触发器的实例，定义该job立即执行，并且每2秒执行一次，一直执行
        SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity("MyTrigger").startNow().withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2).repeatForever()).build();
        // 创建schedule实例
        StdSchedulerFactory factory = new StdSchedulerFactory();
        Scheduler scheduler = factory.getScheduler();
        scheduler.start();
        scheduler.scheduleJob(jobDetail, trigger);

    }
}