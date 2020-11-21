package com.microwu.concurrent.collection.pac;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/15   15:17
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LockCondition {

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition producerCondition = lock.newCondition();
        Condition consumerCondition = lock.newCondition();
        Resource resource = new Resource(lock, producerCondition, consumerCondition);

        ProducerThread p = new ProducerThread(resource);
        ConsumerThread c1 = new ConsumerThread(resource);
        ConsumerThread c2 = new ConsumerThread(resource);

        p.start();
        c1.start();
        c2.start();
    }

    private static class Resource {
        private int num = 0;
        private int size = 10;
        private Lock lock;
        private Condition produceCondition;
        private Condition consumerCondition;

        public Resource(Lock lock, Condition produceCondition, Condition consumerCondition) {
            this.lock = lock;
            this.produceCondition = produceCondition;
            this.consumerCondition = consumerCondition;
        }

        public void add() {
            lock.lock();
            try {
                if (num < size) {
                    num++;
                    System.out.println(Thread.currentThread().getName() + " 生产一件资源，当前资源池有 " + num + " 个。。。");
                    consumerCondition.signalAll();
                } else {
                    try {
                        produceCondition.await();
                        System.out.println(Thread.currentThread().getName() + " 生产者进入等待。。。");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                lock.unlock();
            }
        }

        public void remove() {
            lock.lock();
            try {
                if (num > 0) {
                    num--;
                    System.out.println("消费者 " + Thread.currentThread().getName() + " 消耗一件资源，当前资源池有 " + num + " 个。。。");
                    produceCondition.signalAll();
                } else {
                    try {
                        consumerCondition.await();
                        System.out.println(Thread.currentThread().getName() + " 消费者等待。。。");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private static class ConsumerThread extends Thread {
        private Resource resource;

        public ConsumerThread(Resource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                resource.remove();
            }
        }
    }

    private static class ProducerThread extends Thread {
        private Resource resource;

        public ProducerThread(Resource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                resource.add();
            }
        }
    }

}