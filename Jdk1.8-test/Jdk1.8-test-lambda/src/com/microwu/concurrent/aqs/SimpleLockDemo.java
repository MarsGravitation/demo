package com.microwu.concurrent.aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Description:
 *  1. acquire 方法用来获取锁，true 说明线程获取成功
 *      false 线程加入等待队列，等待被唤醒
 *  2. release 用来释放锁
 *
 * http://zhanjindong.com/2015/03/10/java-concurrent-package-aqs-overview
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/24   15:44
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SimpleLockDemo {

    class SimpleLock extends AbstractQueuedSynchronizer {

        @Override
        protected boolean tryAcquire(int unused) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        public void lock() {
            acquire(1);
        }

        public boolean tryLock() {
            return tryAcquire(1);
        }

        public void unlock() {
            release(1);
        }

        public boolean isLocked() {
            return isHeldExclusively();
        }
    }
}