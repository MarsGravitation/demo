package com.microwu.cxd.quartz.job;

import com.microwu.cxd.quartz.service.MyService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 作业可以定义 setter 来注入数据映射属性。也可以用类似的方式注入普通 bean
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/4  11:17
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class MySampleJob extends QuartzJobBean {

    private String name;

    private MyService myService;

    /**
     * Inject MyService bean
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/4     11:22
     *
     * @param myService
     * @return void
     */
    @Autowired
    public void setMyService(MyService myService) {
        this.myService = myService;
    }

    /**
     * Inject the name job data property
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/4     11:23
     *
     * @param name
     * @return void
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        this.myService.someMethod(jobExecutionContext, this.name);
    }
}
