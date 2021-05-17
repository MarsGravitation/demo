package com.microwu.cxd.component;

import com.microwu.cxd.domain.ResponseMsg;
import com.microwu.cxd.domain.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/5/14  9:40
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
@Component
public class TaskQueue {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskQueue.class);

    private static final int QUEUE_LENGTH = 10;

    private BlockingQueue<Task> queue = new LinkedBlockingDeque<>(QUEUE_LENGTH);

    private int taskId = 10;

    /**
     * 添加任务
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/5/14     9:43
     *
     * @param deferredResult
     * @return void
     */
    public void put(DeferredResult<ResponseMsg<String>> deferredResult) {
        taskId++;

        LOGGER.info("任务加入队列，id 为 {}", taskId);

        queue.offer(new Task(taskId, deferredResult));
    }

    /**
     * 获取任务
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/5/14     9:43
     *
     * @param
     * @return com.microwu.cxd.domain.Task
     */
    public Task task() {
        Task task = queue.poll();

        LOGGER.info("获取任务：{}", task);

        return task;
    }

}
