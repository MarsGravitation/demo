package com.microwu.cxd.common.domain;

import java.util.concurrent.ScheduledFuture;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/9   21:20
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public final class ScheduledTask {

    /**
     * 线程池执行完任务的结果
     */
    volatile ScheduledFuture<?> future;

    /**
     * 取消任务
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/2/9  21:22
     *
     * @param
     * @return  void
     */
    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if(future != null) {
            future.cancel(true);
        }
    }

}