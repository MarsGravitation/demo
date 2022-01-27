package com.microwu.cxd.quartz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 * JobDetail: 定义一个特定的 job。JobDetail 可以使用 JobBuilder 构建
 * Calender:
 * Trigger: 定义何时触发特定作业
 *
 * 默认情况下，使用内存 JobStore 中。但是，如果 DataSource bean 在应用程序中可用并且
 * spring.quartz.job-store-type 配置了属性，则可以配置基于 JDBC 的存储，如下：
 *
 * spring.quartz.job-store-type=jdbc
 *
 * 使用 JDBC 存储时，可以在启动时初始化架构，如下：
 *
 * spring.quartz.jdbc.initialize-schema=always
 *
 * 要让 Quartz 使用 DataSource 应用程序的 main 以外的其他元素 DataSource，请声明一个
 * DataSource bean，并用注释其 @Bean 方法 @QuartzDataSource。这样可以确保和模式初始化
 * DataSource 都是用 Quartz 特定的 SchedulerFactoryBean。类似的，要让 Quartz 使用
 * TransactionManager 应用程序的 main 以外的其他方法 TransactionManager 声明一个 TransactionManager
 * bean，@Bean 用 @QuartzTransactionManager
 *
 * 默认情况下，配置创建的作业不会覆盖已从持久作业存储中读取的已注册作业。要启用覆盖现有作业定义，
 * 请设置 spring.quartz.overwrite-existing-jobs 属性。
 *
 * Quartz Scheduler 配置可以使用允许编程定制的 spring.quartz 属性和 SchedulerFactoryBeanCustomizer
 * bean 进行 SchedulerFactoryBean 定制。高级 Quartz 配置属性可以使用 spring.quartz.properties.*
 *
 * 特别是，Executor bean 与调度器无关，因为 Quartz 提供了一种通过 spring.quartz.properties.
 * 如果您需要自定义任务执行器，请考虑实现 SchedulerFactoryBeanCustomizer
 *
 * 任务执行和调度
 *  在 Executor 上下文中没有 bean 的情况下，SpringBoot 自动配置了一个 ThreadPoolTaskExecutor 具有合理默认值，
 *  这些默认值可以自动关联到异步任务执行(@EnableAsync) 和 SpringMVC 异步请求处理
 *
 *  如果您 Executor 在上下文中定义了自定义，则常规任务执行（@EnableAsync） 将透明使用它，
 *  但是不会配置 Spring MVC 支持，因为它需要一个 AsyncTaskExecutor 实现（名为 applicationTaskExecutor）。
 *  根据您的目标安排，您可以将您的 Executor 更改为 ThreadPoolTaskExecutor 或者同时定义一个 ThreadPoolTaskExecutor
 *  和一个 AsyncConfigurer 包装您的自定义 Executor。
 *
 *  自动配置 TaskExecutorBuilder 允许您轻松创建实例来重现默认情况下自动配置所做的事情。
 *
 *  线程池使用 8 个核心线程，可以根据负载增减。
 *
 *  spring.task.execution.pool.max-size=16
 *  spring.task.execution.pool.queue-capacity=100
 *  spring.task.execution.pool.keep-alive=10s
 *
 *  这会将线程池更改为使用有界队列，以便当队列已满（100 个任务时），线程池增加到最多 16 个线程。
 *  由于线程在空闲 10 s 时被回收，因此池的收缩更加激进
 *
 *  ThreadPoolTaskScheduler 如果需要与计划任务相关联（例如 @EnableScheduling），也可以自动
 *  配置。线程池默认使用一个线程，其设置可以使用 spring.task.scheduling 命名空间微调，如下：
 *
 *  spring.task.scheduling.thread-name-prefix=scheduling
 *  spring.task.scheduling.pool.size=2
 *
 *  TaskExecutorBuilder bean 和 TaskSchedulerBuilder bean 可在上下文提供，如果要创建一个
 *  自定义的遗嘱执行人活调度的需求
 *
 *
 * https://docs.spring.io/spring-boot/docs/2.5.0/reference/htmlsingle/
 *
 * https://www.cnblogs.com/youzhibing/p/10208056.html
 *
 */
@SpringBootApplication
public class SpringQuartzApplication {
    public static void main( String[] args ) {
        SpringApplication.run(SpringQuartzApplication.class,args);
    }
}
