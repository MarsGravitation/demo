package com.microwu.cxd.zookeeper.simple.application;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Description: 数据服务端代码
 *  启动时，把自己以临时节点的形式注册到 zookeeper。一旦服务器挂掉，zookeeper 自动删除临时 znode
 *
 *  数据服务启动时，单线程运行此代码，实现注册到 zookeeper 逻辑，维系和 zookeeper 的连接
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/1   13:38
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ServiceRegister implements Runnable {

    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(1);

    private ZooKeeper zk;

    private static final String ZNODE = "/sas";

    private static final String SA_NODE_PREFIX = "sa_";

    private String hostName = "192.168.133.134:2181";

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public ServiceRegister() throws IOException {
        zk = new ZooKeeper(this.hostName, 10000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println(event);
                COUNT_DOWN_LATCH.countDown();
            }
        });
    }

    @Override
    public void run() {
        try {
            COUNT_DOWN_LATCH.await();
            String saNode = createSaNode();
            System.out.println(saNode);

            synchronized (this) {
                wait();
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String createSaNode() throws KeeperException, InterruptedException {
        // 如果根节点不存在，则创建根节点
        Stat stat = zk.exists(ZNODE, false);
        if (stat == null) {
            zk.create(ZNODE, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

//        String hostname = System.getenv("HOSTNAME");
        // 创建临时有序节点
        return zk.create(ZNODE + "/" + SA_NODE_PREFIX, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    public static void main(String[] args) throws IOException {
        new ServiceRegister().run();
    }
}