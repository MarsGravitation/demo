package com.microwu.cxd.service;

import com.microwu.cxd.domain.Post;
import com.microwu.cxd.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;

/**
 * Description:    如果是SpringBoot的异步, 需要开启@EnableAsync和@Async注解
 *          如果没有配置线程池, 会使用SpringBoot自带异步线程池
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/4   10:00
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class UserQueryFacade {
    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private FollowService followService;

    private static final CountDownLatch count = new CountDownLatch(3);

    private static final ExecutorService executor = Executors.newFixedThreadPool(3);

    public User getById(Long id) throws InterruptedException, ExecutionException {
        Future<User> userFuture = executor.submit(() -> {
            try {
                return userService.getById(id);
            }finally {
                count.countDown();
            }
        });
        Future<List<Post>> postFuture = executor.submit(() -> {
            try {
                return postService.getById(id);
            } finally {
                count.countDown();
            }
        });
        Future<List<User>> listFuture = executor.submit(() -> {
            try {
                return followService.listById(id);
            } finally {
                count.countDown();
                ;
            }
        });
        count.await();
        User user = userFuture.get();
        user.setPosts(postFuture.get());
        user.setFollows(listFuture.get());
        return user;
    }
}