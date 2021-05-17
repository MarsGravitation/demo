package com.microwu.cxd.component;

import com.microwu.cxd.domain.ResponseMsg;
import com.microwu.cxd.domain.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;

/**
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/5/14  9:46
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
@Component
public class TaskExecute {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskQueue.class);

    private static final Random RANDOM = new Random();

    /**
     * 默认随机结果的长度
     */
    private static final int DEFAULT_STR_LEN = 10;

    /**
     * 用于生成随机结果
     */
    private static final String STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Autowired
    private TaskQueue taskQueue;

    /**
     * 初始化启动
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/5/14     9:48
     *
     * @param
     * @return void
     */
    @PostConstruct
    public void init() {
        LOGGER.info("开始持续处理任务");

        new Thread(this::execute).start();
    }

    /**
     * 持续处理，返回执行结果
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/5/14     9:49
     *
     * @param
     * @return void
     */
    private void execute() {
        while (true) {
            try {
                // 取出任务
                Task task;

                synchronized (taskQueue) {
                    task = taskQueue.task();
                }

                if (task != null) {
                    // 设置返回结果
                    String randomStr = getRandomStr(DEFAULT_STR_LEN);

                    ResponseMsg<String> responseMsg = new ResponseMsg<>(0, "success", randomStr);

                    LOGGER.info("返回结果: {}", responseMsg);

                    task.getTaskResult().setResult(responseMsg);
                }

                int time = RANDOM.nextInt(10);

                LOGGER.info("处理间隔: {} 秒", time);

                Thread.sleep(time * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取长度为 len 的随机串
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/5/14     9:54
     *
     * @param len
     * @return java.lang.String
     */
    private String getRandomStr(int len) {

        int maxInd = STRING.length();

        StringBuilder sb = new StringBuilder();

        int ind;

        for (int i = 0; i < len; i++) {
            ind = RANDOM.nextInt(maxInd);
            sb.append(STRING.charAt(ind));
        }

        return String.valueOf(sb);

    }

}
