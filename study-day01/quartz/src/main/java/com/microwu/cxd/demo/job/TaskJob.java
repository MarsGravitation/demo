package com.microwu.cxd.demo.job;

import org.quartz.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description: 具体任务类
 *  Job：是一个接口，只有一个方法void execute(JobExecutionContext)，开发者实现该接口定义运行任务，
 *      JobExecutionContext类提供了调度上下文的各种信息。Job运行时的信息保存在JobDataMap实例中。
 * Author:   Administration
 * Date:     2019/3/18 14:17
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class TaskJob implements Job {
    // 这里是第二种获取JobDataMap中的值
    private String message;
    private float floatJobValue;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public float getFloatJobValue() {
        return floatJobValue;
    }

    public void setFloatJobValue(float floatJobValue) {
        this.floatJobValue = floatJobValue;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 打印当前的执行时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("现在的时间：" + format.format(date));
        // 具体逻辑
        System.out.println("开始生成任务报表 或者 开始发送邮件。。。。");
        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();
        System.out.println("jobDetail的name：" + jobKey.getName());
        System.out.println("JobDetail的group：" + jobKey.getGroup());
//        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
//        String message = jobDataMap.getString("message");
//        float floatJobValue = jobDataMap.getFloat("FloatJobValue");
        System.out.println("jobDataMap定义的message的值：" + message);
        System.out.println("jobDataMap定义的floatJobValue的值：" + floatJobValue);

    }
}