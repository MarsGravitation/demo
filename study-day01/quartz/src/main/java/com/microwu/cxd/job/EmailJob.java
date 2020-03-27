package com.microwu.cxd.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description:
 *  与上一个简单案例的区别在于，SimpleTrigger/CronTrigger。简单的定时任务，可以采用SimpleTrigger，
 *  复杂的任务一般采用CronTrigger.cronTrigger不仅可以设定单的触发时间表，更可以设定非常复杂的出发时间表。
 *  CronTrigger是基于Unix类似于cron表达式
 * Author:   Administration
 * Date:     2019/3/18 11:42
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class EmailJob  implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 打印当前的执行时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("现在的时间是：" + format.format(date));
        // 具体的业务逻辑
        System.out.println("开始生成任务报表 或者 开始发送邮件");
    }
}