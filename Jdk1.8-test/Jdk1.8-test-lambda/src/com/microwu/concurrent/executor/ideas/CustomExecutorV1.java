package com.microwu.concurrent.executor.ideas;

import java.util.concurrent.Executor;

/**
 * 线程池 V1
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/24  17:40
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class CustomExecutorV1 implements Executor {
    @Override
    public void execute(Runnable command) {
        new Thread(command).start();
    }
}
