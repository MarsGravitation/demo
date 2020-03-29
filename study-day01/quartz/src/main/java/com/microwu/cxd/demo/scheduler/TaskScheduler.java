package com.microwu.cxd.demo.scheduler;

import com.microwu.cxd.demo.job.TaskJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Description:
 *  Quartz在每次运行job时，都重新创建一个Job实例，所以它不直接接受一个Job的实例，相反
 *  它接受一个Job实现类，以便运行时通过newInstance()反射机制实例化Job。JobDetail是用来
 *  描述Job的实现类及其它的相关静态信息，如job名字、描述、关联监听器等；
 *  构造器：JobDetail(String name, String group, Class jobClass)，该构造函数要求指定Job的实现类，
 *  以及任务在Scheduler中的组名和job名称
 * Author:   Administration
 * Date:     2019/3/18 14:13
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class TaskScheduler {
    public static void main(String[] args) throws SchedulerException {
        // 1.创建一个JobDetail的实例，将该实例与TaskJob Class绑定
        JobDetail jobDetail = JobBuilder
                .newJob(TaskJob.class) // 定义Job类为TaskJob，真正的执行逻辑所在
                .withIdentity("taskJob", "taskGroup") //定义name和group
                .usingJobData("message", "task...") // 加入属性到jobDataMap
                .usingJobData("FloatJobValue", 8.88f) // 加入属性到jobDataMap
                .build();
        // 2.创建一个Trigger触发器的实例，定义该Job立即执行，并且每2秒钟执行一次，一直执行
        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("taskJob", "taskGroup")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2).repeatForever())
                .build();
        // 创建scheduler实例
        StdSchedulerFactory factory = new StdSchedulerFactory();
        Scheduler schduler = factory.getScheduler();
        schduler.start();
        schduler.scheduleJob(jobDetail, trigger); // JobDetail和trigger加入调度

    }
}