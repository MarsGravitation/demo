package com.microwu.cxd.controller;

import com.microwu.cxd.common.domain.CronTaskRegister;
import com.microwu.cxd.common.domain.SchedulingRunnable;
import com.microwu.cxd.common.domain.TaskJobDO;
import com.microwu.cxd.mapper.DynamicTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/9   20:48
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
public class TestController {
    @Autowired
    private DynamicTaskMapper dynamicTaskMapper;

    @Autowired
    private CronTaskRegister cronTaskRegister;

    @PostMapping("/add")
    public String add(TaskJobDO taskJobDO) {
        dynamicTaskMapper.insert(taskJobDO);
        SchedulingRunnable task = new SchedulingRunnable(taskJobDO.getBeanName(), taskJobDO.getMethodName(), taskJobDO.getMethodParams());
        cronTaskRegister.addCronTask(task, taskJobDO.getCronExpression());
        return "success";
    }

}