package com.microwu.cxd.zookeeper.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Description: 分布式锁
 *  1.0 版本
 *      1. zookeeper 中一个临时节点代表锁，比如在 /exlusive_lock 下创建临时子节点 /exlusive_lock/lock
 *      2. 所有客户端创建此节点，但是只有一个客户端创建成功（同步创建方式，如果存在该节点会抛出异常）
 *      3. 创建成功代表获取锁成功，次客户端执行业务逻辑
 *      4. 未创建成功的客户端，监听 /exlusive_lock 变更
 *      5. 获取锁的客户端执行完成后，删除 /exlusive_lock/lock，表示锁释放
 *      6. 其他监听 /exlusive_lock 变更的客户端得到通知，再次竞争
 *  缺点：1. 不公平；2. 羊群效应
 *
 *  2.0 版本
 *      1. 每个客户端往 /exlusive_lock 下创建有序临时节点
 *      2. 客户端获取所有子节点，排序，判断最前面的是否为自己
 *      3. 如果自己在锁节点的第一位，获取锁
 *      4. 如果不在第一位，监听自己前一位锁节点
 *      5. 当前一位锁节点执行完，触发了后一位的逻辑
 *      6. 重新执行第二步逻辑
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/27   17:26
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LockSample {

    private ZooKeeper zkClient;
    private static final String LOCK_ROOT_PATH = "/Locks";
    private static final String LOCK_NODE_NAME = "Lock_";
    private String lockPath;

    private Watcher watcher = new Watcher() {
        @Override
        public void process(WatchedEvent watchedEvent) {
            System.out.println(watchedEvent.getPath() + " 前锁释放");
            synchronized (this) {
                notifyAll();
            }
        }
    };

    public LockSample() throws IOException {
        zkClient = new ZooKeeper("192.168.133.134:2181", 10000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState() == Event.KeeperState.Disconnected) {
                    System.out.println("失去连接");
                }
            }
        });
    }

    public void acquireLock() throws KeeperException, InterruptedException {
        // 创建锁节点
        createLock();
        // 尝试获取锁
        attemptLock();
    }

    private void createLock() throws KeeperException, InterruptedException {
        // 如果根节点不存在，则创建根节点
        Stat stat = zkClient.exists(LOCK_ROOT_PATH, false);
        if (stat == null) {
            zkClient.create(LOCK_ROOT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        // 创建 EPHEMERAL_SEQUENTIAL 类型节点
        String lockPath = zkClient.create(LOCK_ROOT_PATH + "/" + LOCK_NODE_NAME, Thread.currentThread().getName().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(Thread.currentThread().getName() + " 锁创建：" + lockPath);
        this.lockPath = lockPath;
    }

    private void attemptLock() throws KeeperException, InterruptedException {
        // 获取 Lock 所有的子节点，按照节点序号排序
        List<String> lockPaths = null;
        lockPaths = zkClient.getChildren(LOCK_ROOT_PATH, false);

        Collections.sort(lockPaths);

        int index = lockPaths.indexOf(lockPath.substring(LOCK_ROOT_PATH.length() + 1));

        // 如果 lockPath 是序号最小的节点，则获取锁
        if (index == 0) {
            System.out.println(Thread.currentThread().getName() + " 锁获得，lockPath：" + lockPath);
        } else {
            // lockPath 不是序号最小的节点，监听前一个节点
            String preLockPath = lockPaths.get(index - 1);
            Stat stat = zkClient.exists(LOCK_ROOT_PATH + "/" + preLockPath, watcher);

            // 加入前一个节点不存在了，重新获取锁
            if (stat == null) {
                attemptLock();
            } else {
                // 阻塞当前线程，直到 preLockPath 释放锁，被 watcher 观察到，notifyAll 后，重新 acquireLock
                synchronized (watcher) {
                    watcher.wait();
                }
                attemptLock();
            }
        }
    }

    public void releaseLock() throws KeeperException, InterruptedException {
        zkClient.delete(lockPath, -1);
        zkClient.close();
        System.out.println("锁释放：" + lockPath);
    }

}