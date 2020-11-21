package com.microwu.concurrent.collection.pac;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/15   14:27
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ProducerConsumerWithWaitNotify {

    public static void main(String[] args) {
        Resource resource = new Resource();

        ProducerThread producerThread = new ProducerThread(resource);
        ConsumerThread consumerThread = new ConsumerThread(resource);
        ConsumerThread consumerThread2 = new ConsumerThread(resource);

        producerThread.start();
        consumerThread.start();
        consumerThread2.start();

    }

    /**
     * Resource 是共享资源
     *
     *  add/remove：锁住的是 this，也就是 resource
     *  而唤醒也是 resource 进行唤醒
     *
     *  synchronized：保证同一时刻，只有一个线程可以执行某个方法或者某个代码块，主要是对方法或者代码块存在的共享数据操作，
     *      同时，可保证现成的变化，主要是共享数据的变化被其他线程所看到（volatile 功能）
     *
     *  修饰实例方法：进入代码要获得当前实例的锁
     *  修饰静态方法：获取类对象的锁
     *  修饰代码块：获取给定对象的锁
     *
     *  entryList：
     *  owner：
     *  waitSet：
     *
     *  当多个线程同时访问一段同步代码时，首先会进入 EntryList，当线程获取到对象的 monitor 后进入 owner 区域，并把 monitor 的 owner
     *  变量设置为当前线程，同时 monitor 中的计数器 count + 1，若线程调用 wait 方法，将释放当前持有的 monitor，owner 变量恢复为 null，
     *  count - 1，同时该线程进入 waitSet 等待被唤醒。若当前县城执行完毕将释放 monitor 并复位变量的值，以便其他线程进入获取 monitor 锁
     */
    private static class Resource {
        private int num = 0;

        private int size = 10;

        public synchronized void remove() {
            if (num > 0) {
                num--;
                System.out.println("消费者：" + Thread.currentThread().getName() + " 消耗资源，还剩资源 " + num + " 个。。。");
                this.notifyAll();
            } else {
                try {
                    this.wait();
                    System.out.println("消费者：" + Thread.currentThread().getName() + " 进入等待状态。。。");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public synchronized void add() {
            if (num < size) {
                num++;
                System.out.println(Thread.currentThread().getName() + " 生产一个资源，还剩资源 " + num + " 个。。。");
                this.notifyAll();
            } else {
                try {
                    this.wait();
                    System.out.println("生产者：" + Thread.currentThread().getName() + " 进入等待状态。。。");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
            while (true) {
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