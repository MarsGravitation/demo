package com.microwu.cxd.quartz.config;

import com.microwu.cxd.quartz.job.FirstJob;
import com.microwu.cxd.quartz.job.MySampleJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 调度器 Scheduler 绑定
 *
 *  Scheduler 绑定有两种方式，一种是使用 bean 的自动配置，一种是 Scheduler 手动配置
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/4  13:38
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
@Configuration
public class QuartzConfig {

    private static final String ID = "SUMMER_DAY";

    @Bean
    public JobDetail printTimeJobDetail() {
        // MySampleJob 我们的业务类
        return JobBuilder.newJob(MySampleJob.class)
                // 该 JobDetail 起一个 id
                .withIdentity("mySampleJob")
                // 每个 JobDetail 内部都有一个 map，包含了关联到这个 Job 的数据，在 Job 类中可以通过 context 获取
                .usingJobData("msg", "hello")
                .usingJobData("name", "cxd")
                // 即使没有 Trigger 关联时，也不需要删除该 JobDetail
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger printTimeJobTrigger() {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/1 * * * * ?");
        return TriggerBuilder.newTrigger()
                // 关联上述的 JobDetail
                .forJob(printTimeJobDetail())
                // 给 Trigger 起个名字
                .withIdentity("quartzTaskService")
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(FirstJob.class)
                .withIdentity(ID + " 01")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger trigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(5)
                .repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withIdentity(ID + " 01Trigger")
                .withSchedule(scheduleBuilder)
                .build();
    }

}
