package com.microwu.cxd;

import com.microwu.cxd.controller.UserController;
import com.microwu.cxd.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/8   11:16
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SpringBootTest
@Async
@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserController userController;

    @Test
    public void test() throws InterruptedException {
        System.out.println(Thread.currentThread() + ": 主线程开始了");
        userService.sendMail();
        userService.sendMsg();
    }

    @Test
    public void test02() throws InterruptedException {
        userController.register();
    }
}