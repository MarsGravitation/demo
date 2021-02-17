package com.microwu.cxd.mybatis;

import com.microwu.cxd.mybatis.pojo.User;
import com.microwu.cxd.mybatis.service.FooService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/21   16:26
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@DirtiesContext
public abstract class AbstractSampleTest {

    @Autowired
    private FooService fooService;

    @Test
    public void testFooService() {
        User user = this.fooService.doSomeBusinessStuff("u1");
        System.out.println(user);
    }

}