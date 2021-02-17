package com.microwu.cxd.common.construct;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   15:19
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class RingBufferWheel {

    private static final int STATIC_RING_SIZE = 64;

    private Object[] ringBuffer;

    private int bufferSize;

    private ExecutorService executorService;

    private volatile int size;

    private volatile boolean stop = false;

    private AtomicBoolean start = new AtomicBoolean(false);

    private AtomicInteger tick = new AtomicInteger();

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    private AtomicInteger taskId = new AtomicInteger();
    private Map<Integer, Task> taskMap = new ConcurrentHashMap<>(16);

    public RingBufferWheel(ExecutorService executorService) {
        this.executorService = executorService;
        this.bufferSize = STATIC_RING_SIZE;
        this.ringBuffer = new Object[bufferSize];
    }

    public int addTask(Task task) {
        int key = task.getKey();
        int id;
        lock.lock();
        try {
            int index = mod(key, bufferSize);
            task.setIndex(index);
            Set<Task> tasks = get(index);

            int cycleNum = cycleNum(key, bufferSize);
            if (tasks != null) {
                task.setCycleNum(cycleNum);
                tasks.add(task);
            } else {
                task.setIndex(index);
                task.setCycleNum(cycleNum);
                HashSet<Task> sets = new HashSet<>();
                sets.add(task);
                put(key, sets);
            }
            id = taskId.incrementAndGet();
            task.setTaskId(id);
            taskMap.put(id, task);
            size++;
        } finally {
            lock.unlock();
        }

        start();

        return id;
    }

    public boolean cancel(int id) {
        boolean flag = false;
        Set<Task> tempTask = new HashSet<>();

        lock.lock();
        try {
            Task task = taskMap.get(id);
            if (task == null) {
                return false;
            }

            Set<Task> tasks = get(task.getIndex());
            for (Task t : tasks) {
                if (t.getKey() == task.getKey() && t.getCycleNum() == task.getCycleNum()) {
                    size--;
                    flag = true;
                    taskMap.remove(id);
                } else {
                    tempTask.add(t);
                }
            }

            ringBuffer[task.getIndex()] = tempTask;
        } finally {
            lock.unlock();
        }
        return flag;
    }

    public void start() {
        if (!start.get()) {
            if (start.compareAndSet(start.get(), true)) {
                Thread job = new Thread(new TriggerJob());
                job.start();
                start.set(true);
            }
        }
    }

    public void stop(boolean force) {
        if (force) {
            stop = true;
            executorService.shutdown();
        } else {
            if (taskSize() > 0) {
                lock.lock();
                try {
                    condition.await();
                    stop = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            executorService.shutdown();
        }
    }


    private Set<Task> remove(int key) {
        Set<Task> tempTask = new HashSet<>();
        Set<Task> result = new HashSet<>();

        Set<Task> tasks = (Set<Task>) ringBuffer[key];
        if (tasks == null) {
            return result;
        }

        for (Task task : tasks) {
            if (task.getCycleNum() == 0) {
                result.add(task);

                size2Notify();
            } else {
                task.setCycleNum(task.getCycleNum() - 1);
                tempTask.add(task);
            }

            taskMap.remove(task.getTaskId());
        }

        ringBuffer[key] = tempTask;

        return result;
    }

    private void size2Notify() {
        lock.lock();
        try {
            size--;
            if (size == 0) {
                condition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    private int taskSize() {
        return size;
    }

    private void put(int key, Set<Task> tasks) {
        int index = mod(key, bufferSize);
        ringBuffer[index] = tasks;
    }

    private int cycleNum(int target, int mod) {
        return target >> Integer.bitCount(mod - 1);
    }

    private Set<Task> get(int index) {
        return (Set<Task>) ringBuffer[index];
    }

    private int mod(int target, int mod) {
        target = target + tick.get();
        return target % (mod - 1);
    }

    public abstract static class Task extends Thread {
        private int index;

        /**
         * 圈数
         */
        private int cycleNum;

        /**
         * 延时时间
         */
        private int key;

        private int taskId;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getCycleNum() {
            return cycleNum;
        }

        public void setCycleNum(int cycleNum) {
            this.cycleNum = cycleNum;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public int getTaskId() {
            return taskId;
        }

        public void setTaskId(int taskId) {
            this.taskId = taskId;
        }
    }

    private class TriggerJob implements Runnable {
        @Override
        public void run() {
            int index = 0;
            while (!stop) {
                try {
                    Set<Task> tasks = remove(index);
                    tasks.forEach(task -> executorService.submit(task));

                    if (++index > bufferSize - 1) {
                        index = 0;
                    }

                    tick.incrementAndGet();
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}