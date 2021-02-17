package com.microwu.structure;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description: 时间轮
 *
 * https://github.com/crossoverJie/cim/blob/master/cim-common/src/main/java/com/crossoverjie/cim/common/data/construct/RingBufferWheel.java
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/14   10:15
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class RingBufferWheel {

//    private static final Object[] ringBuffer = new Object[8];
//
//    private static final Lock lock = new ReentrantLock();
//
//    private static final ExecutorService executorService = new ThreadPoolExecutor(1, 1, 60 , TimeUnit.SECONDS, new ArrayBlockingQueue<>(1));
//
//    public void start() {
//        // 启动一个任务，轮训取任务，可以用线程池，也可以死循环
//        new Thread(new RemoveRunnable()).start();
//    }
//
//    public void addTask(DelayTask delayTask) {
//        // 1. 获取时间
//        int key = delayTask.getKey();
//        // 2. 通过时间计算下标
//        int index = key % ringBuffer.length;
//        // 3. 计算圈数
//        int cycleNum = key / ringBuffer.length;
//        // 这里不能用 list，因为一秒钟可能有多个任务，如果用 list 就会覆盖
//        // 使用 set，取的时候每次遍历，先把所有的圈数都减一，然后圈数等于 0 的取出来
//        Set<DelayTask> delayTasks = (Set<DelayTask>) ringBuffer[index];
//        lock.lock();
//        try {
//            // 4. 判断是否为第一次创建数组
//            if (delayTasks == null || delayTasks.size() == 0) {
//                delayTasks = new HashSet<>();
//                ringBuffer[index] = delayTasks;
//            }
//            delayTask.cycleNum = cycleNum;
//            delayTasks.add(delayTask);
//        } finally {
//            lock.unlock();
//        }
//
//    }
//
//    public void removeTask(int key) {
//        // 1. 获取下标
//        int index = key % ringBuffer.length;
//        // 2. 获取数组
//        Set<DelayTask> delayTasks = (Set<DelayTask>)ringBuffer[index];
//        lock.lock();
//        try {
//            // 判空
//            if (delayTasks == null || delayTasks.size() == 0) {
//                return;
//            }
//            // 如果不为空，遍历，将圈数减一，同时取出为 0 的 任务，执行任务
//            final List<DelayTask> runTasks = new ArrayList<>();
//            delayTasks.stream().forEach(delayTask -> {
//                if (delayTask.cycleNum == 0) {
//                    runTasks.add(delayTask);
//                }
//                delayTask.cycleNum--;
//            });
//            // 执行任务
//            executorService.invokeAll(runTasks);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            lock.unlock();
//        }
//    }
//
//
//    public static class DelayTask implements Callable<Void> {
//
//        /**
//         * 延时时间
//         */
//        private int key;
//
//        /**
//         * 圈数
//         */
//        private int cycleNum;
//
//        public int getKey() {
//            return key;
//        }
//
//        public void setKey(int key) {
//            this.key = key;
//        }
//
//        public int getCycleNum() {
//            return cycleNum;
//        }
//
//        public void setCycleNum(int cycleNum) {
//            this.cycleNum = cycleNum;
//        }
//
//
//        @Override
//        public Void call() throws Exception {
//            System.out.println("Key: " + key + ", Time: " + LocalDateTime.now());
//            return null;
//        }
//    }
//
//    public class RemoveRunnable implements Runnable {
//
//        private int pointer = 0;
//
//        @Override
//        public void run() {
//            while (!Thread.interrupted()) {
//                // 转到结尾，恢复为 0
//                if (pointer == ringBuffer.length - 1) {
//                    pointer = 0;
//                }
//                removeTask(pointer);
//                pointer++;
//                try {
//                    TimeUnit.SECONDS.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public static void main(String[] args) throws InterruptedException {
//        System.out.println("Start time: " + LocalDateTime.now());
//        RingBufferWheel ringBufferWheel = new RingBufferWheel();
//        ringBufferWheel.start();
//        DelayTask t1 = new DelayTask();
//        t1.setKey(5);
//        ringBufferWheel.addTask(t1);
//        DelayTask t2 = new DelayTask();
//        t2.setKey(10);
//        ringBufferWheel.addTask(t2);
//        DelayTask t3 = new DelayTask();
//        t3.setKey(21);
//        ringBufferWheel.addTask(t3);
//
//        // 阻塞主线程
//        TimeUnit.SECONDS.sleep(30);
//
//    }

    private static final int STATIC_RING_SIZE = 64;

    private Object[] ringBuffer;

    private int bufferSize;

    private ExecutorService executorService;

    /**
     * 正在运行的任务
     */
    private volatile int size;

    /**
     * 停止标识
     */
    private volatile boolean stop;

    /**
     * 开始标识
     */
    private volatile AtomicBoolean start = new AtomicBoolean(false);

    /**
     * 所有记录数，执行一条加一
     */
    private AtomicInteger tick = new AtomicInteger();

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    private AtomicInteger taskId = new AtomicInteger();
    private Map<Integer, Task> taskMap = new ConcurrentHashMap<>();

    public RingBufferWheel() {
        this.executorService = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1));
        this.bufferSize = STATIC_RING_SIZE;
        this.ringBuffer = new Object[bufferSize];
    }


    public int addTask(Task task) {
        int key = task.key;
        int id;
        lock.lock();
        try {
            int index = mod(key, bufferSize);
            task.index = index;
            Set<Task> tasks = get(index);

            int cycleNum = cycleNum(key, bufferSize);
            if (tasks != null) {
                task.cycleNum = cycleNum;
                tasks.add(task);
            } else {
                task.index = index;
                task.cycleNum = cycleNum;
                Set<Task> sets = new HashSet<>();
                sets.add(task);
                put(key, sets);
            }
            id = taskId.incrementAndGet();
            task.taskId = id;
            taskMap.put(id, task);
            size++;
        } finally {
            lock.unlock();
        }

        start();

        return id;
    }

    /**
     * remove 是单线程执行的，但是我认为这里需要加锁
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/14  13:47
     *
     * @param   	key
     * @return  java.util.Set<com.microwu.structure.RingBufferWheel.Task>
     */
    private Set<Task> remove(int key) {
        HashSet<Task> tempTask = new HashSet<>();
        Set<Task> result = new HashSet<>();

        Set<Task> tasks = (Set<Task>) ringBuffer[key];
        if (tasks == null) {
            return null;
        }

        for (Task task : tasks) {
            if (task.cycleNum == 0) {
                result.add(task);

                size2Notify();
            } else {
                task.cycleNum--;
                tempTask.add(task);
            }
            taskMap.remove(task.taskId);
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

    public void start() {
        if (!start.get()) {
            if (start.compareAndSet(start.get(), true)) {
                Thread job = new Thread(new TriggerJob());
                job.setName("consumer ringBuffer thread");
                job.start();
            }
        }

    }

    public void stop(boolean force) {
        if (force) {
            stop = true;
            executorService.shutdownNow();
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

    public boolean cancel(int id) {
        boolean flag = false;
        Set<Task> tempTask = new HashSet<>();

        lock.lock();
        try {
            Task task = taskMap.get(id);
            if (task == null) {
                return false;
            }

            Set<Task> tasks = get(task.index);
            for (Task tk : tasks) {
                if (tk.key == task.key && tk.cycleNum == task.cycleNum) {
                    size--;
                    flag = true;
                    taskMap.remove(id);
                } else {
                    tempTask.add(tk);
                }
            }

            ringBuffer[task.index] = tempTask;
        } finally {
            lock.unlock();
        }

        return flag;
    }

    public int taskSize() {
        return size;
    }

    public int taskMapSize() {
        return taskMap.size();
    }

    private Set<Task> get(int index) {
        return (Set<Task>) ringBuffer[index];
    }

    private void put(int key, Set<Task> tasks) {
        int index = mod(key, bufferSize);
        ringBuffer[index] = tasks;
    }

    /**
     * 模运算
     * 结论：a % 2^n = a & (^2n - 1)
     *
     * https://blog.csdn.net/sww_ang/article/details/93380893
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/14  11:28
     *
     * @param   	key
     * @param 		bufferSize
     * @return  int
     */
    private int mod(int key, int bufferSize) {
        key = key + tick.get();
        return key & (bufferSize - 1);
    }

    /**
     * 除运算
     *
     * 结论：A, B = 2^n
     * A * B = A << n
     * A / B = = A >> n
     * A % B = A % (B - 1)
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/14  11:39
     *
     * @param   	target
     * @param 		mod
     * @return  int
     */
    private int cycleNum(int target, int mod) {
        return target >> Integer.bitCount(mod - 1);
    }

    public class Task extends Thread {
        private int index;
        private int cycleNum;
        private int key;
        private int taskId;
    }

    private class TriggerJob implements Runnable {

        @Override
        public void run() {
            int index = 0;
            while (!stop) {
                Set<Task> tasks = remove(index);
                for (Task task : tasks) {
                    executorService.submit(task);
                }

                if (++index > bufferSize - 1) {
                    index = 0;
                }

                tick.incrementAndGet();
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}