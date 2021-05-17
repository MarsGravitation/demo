package com.microwu.cxd.controller.deferred;

import com.microwu.cxd.component.TaskQueue;
import com.microwu.cxd.domain.ResponseMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * https://blog.csdn.net/m0_37595562/article/details/81013909
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/5/14  9:57
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
@RestController
public class TaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    /**
     * 超时结果
     */
    private static final ResponseMsg<String> OUT_OF_TIME_RESULT = new ResponseMsg<>(-1, "超时", "out of time");

    /**
     * 超时时间
     */
    private static final long OUT_OF_TIME = 3000L;

    @Autowired
    private TaskQueue taskQueue;

    /**
     * 场景一：
     *  1. 创建一个持续在随机间隔时间后从任务队列中获取任务的线程
     *  2. 访问 controller 中的方法，创建一个 DeferredResult，设定超时时间和超时返回对象
     *  3. 设定 DeferredResult 的超时方法和完成回调方法
     *  4. 将 DeferredResult 放入任务中，并将任务放入任务队列
     *  5. 步骤 1 中的线程获取到任务队列中的任务，并产生一个随机结果返回
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/5/14     10:07
     *
     * @param
     * @return org.springframework.web.context.request.async.DeferredResult<com.microwu.cxd.domain.ResponseMsg<java.lang.String>>
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public DeferredResult<ResponseMsg<String>> getResult() {
        LOGGER.info("接受请求，开始处理。。。");

        // 建立 DeferredResult 对象，设置超时时间，以及超时返回超时结果
        DeferredResult<ResponseMsg<String>> result = new DeferredResult<>(OUT_OF_TIME);

        result.onTimeout(() -> {
            result.setResult(OUT_OF_TIME_RESULT);
            LOGGER.info("调用超时");
        });

        result.onCompletion(() -> {
            LOGGER.info("调用完成");
        });

        // 并发，加锁
        synchronized (taskQueue) {
            taskQueue.put(result);
        }

        LOGGER.info("接受任务线程完成退出");

        return result;
    }

}
