package com.microwu.cxd.common.config;

import com.microwu.cxd.common.domain.CronTaskRegister;
import com.microwu.cxd.common.domain.SchedulingRunnable;
import com.microwu.cxd.common.domain.TaskJobDO;
import com.microwu.cxd.mapper.DynamicTaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/20   21:05
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class SysJobRunner implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(SysJobRunner.class);

    @Autowired
    private DynamicTaskMapper dynamicTaskMapper;

    @Autowired
    private CronTaskRegister cronTaskRegister;

    @Override
    public void run(String... args) throws Exception {
        logger.info("初始化定时任务 ...");
        // 初始化加载数据库里状态为正常的定时任务
        List<TaskJobDO> taskJobDOS = dynamicTaskMapper.list();
        if (!CollectionUtils.isEmpty(taskJobDOS)) {
            for (TaskJobDO taskJobDO : taskJobDOS) {
                SchedulingRunnable schedulingRunnable = new SchedulingRunnable(taskJobDO.getBeanName(), taskJobDO.getMethodName(), taskJobDO.getMethodParams());
                cronTaskRegister.addCronTask(schedulingRunnable, taskJobDO.getCronExpression());

            }
            logger.info("初始化定时任务 = {} 完毕 ~~~", taskJobDOS);
        }
    }
}