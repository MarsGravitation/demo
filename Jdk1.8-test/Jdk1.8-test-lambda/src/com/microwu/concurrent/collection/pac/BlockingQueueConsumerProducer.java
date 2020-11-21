package com.microwu.concurrent.collection.pac;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/15   15:33
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BlockingQueueConsumerProducer {

    public static void main(String[] args) {
        Resource resource = new Resource();

        new Thread(new ProducerRunner(resource)).start();
        new Thread(new ConsumerRunner(resource)).start();
        new Thread(new ConsumerRunner(resource)).start();
    }

    private static class Resource {
        private BlockingQueue<Integer> resourceQueue = new LinkedBlockingQueue<>(10);

        public void add() {
            try {
                resourceQueue.put(1);
                System.out.println("生产者 " + Thread.currentThread().getName() + " 生产了一件资源，当前资源池有 " + resourceQueue.size() + " 个资源。。。");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void remove() {
            try {
                resourceQueue.take();
                System.out.println("消费者 " + Thread.currentThread().getName() + "消费了一件资源，当前资源池有 " + resourceQueue.size() + " 件资源。。。");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ConsumerRunner implements Runnable {
        private Resource resource;

        public ConsumerRunner(Resource resource) {
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

    private static class ProducerRunner implements Runnable {
        private Resource resource;

        public ProducerRunner(Resource resource) {
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