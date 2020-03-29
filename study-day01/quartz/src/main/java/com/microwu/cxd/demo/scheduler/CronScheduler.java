package com.microwu.cxd.demo.scheduler;

import com.microwu.cxd.demo.job.EmailJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Description: 任务触发类
 *  Quartz对任务调度的领域问题进行了高度的抽象，提出了调度器、任务和触发器这3个核心的概念
 *  ，并在org.quartz通过接口和类对重要的这些核心概念进行了描述：
 *      1.job：是一个接口，只有一个方法void execute(JobExecutionContext context)，开发者实现该接口定义运行
 *      的任务，JobExecutionContext类提供了调度上下文的各种信息。Job运行时的信息保存在JobDataMap实例中；
 *      2.JobDetail：Quartz在每次执行Job时，都重新创建一个Job实例，所以它不直接接受一个Job实例，相反她接受
 *      一个Job的实现类，以便运行时通过newInstance()的反射机制实例化Job。因此需要通过一个类来描述Job的实现类及其
 *      他相关的静态信息，如Job名字、描述、关联监听器等信息，JobDetail承担了这一个角色
 *          通过该类的构造函数可以更加具体的了解它的功用：JobDetail(java.lang.String name,java.lang.String group,
 *      java.lang.Class jobClass),该构造函数要求指定Job的实现类，以及任务在Scheduler中的组名和Job名称
 *      3.Trigger：是一个类，描述触发Job任务执行的事件触发规则。主要有SimpleTrigger和CronTrigger两个子类。当仅需
 *      触发一次或者以固定时间间隔周期执行，SimpleTrigger是最适合的选择；而CronTrigger则可以通过Cron表达式
 *      定义处各种复杂时间规则的调度方案
 *      4.Calendar：org.quartz.Calendar和java.util.Calendar不同，他是一些日历特定时间点的集合（可以简单的将org.quartz.Calendar
 *      看作java.util.Calendar的集合 ----- java.util.Calendar代表一个日历时间点）一个Trigger可以和多个Calendar关联，以便
 *      排除或包含某些时间点
 *          假设，我们安排每周星期一早上10：00执行任务，但是如果碰到法定节日，任务则不执行，这时就需要在Trigger
 *      触发机制的基础上使用Calendar进行定点排除。针对不同时间段类型，Quartz在org.quartz.impl.calendar包下提供了如干戈Calendar
 *      的实现类
 *      5.Scheduler：代表一个Quartz的独立运行容器，Trigger和JobDetail可以注册到Scheduler中，两者在Scheduler中
 *      拥有各自的组及名称，组及名称是Scheduler查找定位容器中某一对象的一局，Trigger的组即名称必须唯一，
 *      JobDetail的组和名称也必须唯一（但可以和Trigger的组和名称相同，因为他们是不同类型的）。Scheduler定义了多个接口方法，
 *      允许外部通过组及名称访问和控制容器中的Trigger和JobDetail。
 *          Scheduler可以将Trigger绑定到某一个JobDetail中，这样当Trigger触发时，对应的Job就被执行。一个Job可以对应
 *      多个Trigger，但一个Trigger只能对应一个Job。可以个通过SchedulerFactory创建一个Scheduler实例。Scheduler拥有一个SchedulerContext，
 *      它类似于ServletContext，保存着Scheduler上下文信息，Job和Trigger都可以访问SchedulerContext内的信息。SchedulerContext内部通过一个
 *      map，以键值对的形式维护这些上下文数据，SchedulerContext为了保存和获取数据提供了多个put()和getXXX()的方法。可以通过Scheduler#getContext()
 *      获取对应的SchedulerContext实例；
 * Author:   Administration
 * Date:     2019/3/18 11:47
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class CronScheduler {
    public static void main(String[] args) throws SchedulerException {
        // jobDetail
        JobDetail jobDetail = JobBuilder.newJob(EmailJob.class).withIdentity("cronJob").build();
        // cronTrigger --- 每日的9点40触发任务
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("cronTrigger").withSchedule(CronScheduleBuilder.cronSchedule("0 54 11 * * ? ")).build();
        // Scheduler实例
        StdSchedulerFactory factory = new StdSchedulerFactory();
        Scheduler scheduler = factory.getScheduler();
        scheduler.start();
        scheduler.scheduleJob(jobDetail, cronTrigger);

    }
}