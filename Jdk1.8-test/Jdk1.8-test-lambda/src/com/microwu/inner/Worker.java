package com.microwu.inner;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/5   15:03
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Worker {

    private List<Job> mJobList = new ArrayList<>();

    public void addJob(Runnable task) {
        mJobList.add(new Job(task));
    }

    private class Job implements Runnable {
        Runnable task;

        public  Job(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            task.run();
            System.out.println("left job size : " + mJobList.size());
        }
    }
}