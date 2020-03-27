package com.microwu.cxd;

import com.microwu.cxd.common.domain.TaskJobDO;
import com.microwu.cxd.mapper.DynamicTaskMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/9   21:03
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DynamicTaskMapperTest {
    @Autowired
    private DynamicTaskMapper dynamicTaskMapper;

    @Test
    public void test() {
        TaskJobDO taskJobDO = new TaskJobDO();
        taskJobDO.setBeanName("a");
        taskJobDO.setMethodName("b");
        taskJobDO.setMethodParams("c");
        taskJobDO.setCronExpression("d");
        dynamicTaskMapper.insert(taskJobDO);
    }

}