package com.microwu.cxd.service;

import com.microwu.cxd.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/4   14:56
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserQueryFacadeTest {
    @Autowired
    private UserQueryFacade userQueryFacade;

    @Test
    public void test() throws ExecutionException, InterruptedException {
        User user = userQueryFacade.getById(1L);
        System.out.println(user);
    }
}