package com.microwu.cxd.demo3;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Description:
 *
 * https://www.cnblogs.com/summerday152/p/14192845.html
 *
 * 超重要 API
 *
 * Scheduler 和调度程序交互的主要 API
 *  - 生命周期从 SchedulerFactory 创建它开始，到调用 shutdown 方法结束
 *  - 一旦 Scheduler 创建，任何关于 scheduling 相关的事，它都为所欲为：添加、删除、
 *  列出所有的 Jobs 和 triggers、暂停触发器等
 *  - 在 start 方法之前，不会做任何事情
 *
 * Job 你希望被调度器调度的任务组件接口
 *  - 当 Job 的触发器触发时，调度程序的工作线程将调用 execute 方法
 *  - 该方法接受一个 JobExecutionContext 对象，未 Job 实例提供了丰富的运行时环境信
 *  息，比如：scheduler，trigger，jobDataMap，job，calendar，各种 time 等
 *
 * JobDetail 用于定义任务
 *  - JobDetail 对象由 Quartz 客户端在将 job 加入 Scheduler 提供，也就是你的程序。
 *  - 它包含了不同为 job 设置的属性，还有可以用来为 job 存储状态信息的 JobDataMap
 *  - 注意它和 Job 的区别，它实际上是 Job 实例的属性。【Job 定义如何执行，JobDetail 定
 *  义有何属性】
 *
 * Trigger 触发任务执行
 *  - 触发器可能具有与之关联的 JobDataMap，以便于将特定于触发器触发的参数传递给
 *  Job
 *  - Quartz 提供了几种不同的触发器，SimpleTrigger 和 CronTrigger 比较常用。
 *  - 如果你需要一次性执行作业或需要在给定的时间触发一个作业并重复执行 N 次且有两
 *  次执行间有延时 delay，SimpleTrigger 较为方便
 *  - 如果你希望基于类似日期表触发执行任务，CronTrigger 推荐使用
 *
 * JobBuilder 用于构建 JobDetail
 * TriggerBuilder 用于构建 Trigger
 *
 * 重要概念
 *  Identity
 *  - 当作业和触发器在 Quartz 调度程序中测试，会获得标志键
 *  - JobKey 和 TriggerKey 允许被置入 group 中，易于组织管理
 *  - 唯一的，是 name 和 group 的组合标识
 *
 *  JobDataMap
 *  - 是 Map 的实现，具有 k-v 相关操作，存储可序列化数据对象，供 Job 实例在执行时
 *  使用
 *  - 可以使用 usingJobData(k, v) 在构建 JobDetail 的时候传入，使用
 *  jobDetail.getJobDataMap() 获取 map
 *
 *
 * @author: chengxudong             chengxudong@microwu.com
 * Date:       2022/5/18   17:20
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class QuartzTest {
    public static void main(String[] args) throws SchedulerException {
        System.out.println("start...");
        // 从工厂中获取 Scheduler 实例
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 开始
        scheduler.start();

        // 定义 Job，并将其绑定到 HelloJob 类中
        JobDetail job = JobBuilder.newJob(HelloJob.class)
                .withIdentity("job1", "group1")
                .usingJobData("username", "cxd")
                .usingJobData("age", "27")
                .withDescription("desc-demo")
                .build();

        // 触发 Job 执行，每 40s 执行一次
        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(2)
                        .repeatForever())
                .build();

        // 告诉 quartz 使用 trigger 来调度 job
        scheduler.scheduleJob(job, trigger);

        // 关闭，线程终止
//        scheduler.shutdown();
    }
}