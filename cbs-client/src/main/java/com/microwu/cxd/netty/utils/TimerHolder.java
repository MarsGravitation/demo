package com.microwu.cxd.netty.utils;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;

import java.util.concurrent.TimeUnit;

/**
 * Description: A singleton holder of the timer for timeout.
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/29   22:14
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class TimerHolder {

    private static final long DEFAULT_DURATION = 1000;

    private static class DefaultInstance {
        static final Timer INSTANCE = new HashedWheelTimer(new NamedThreadFactory("DefaultTimer-", true), DEFAULT_DURATION, TimeUnit.MILLISECONDS);
    }

    private TimerHolder() {

    }

    public static Timer getInstance() {
        return DefaultInstance.INSTANCE;
    }

}