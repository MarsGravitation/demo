package com.microwu.cxd.zookeeper.simple;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description:
 *  1. ZooKeeper 客户端库的主要类，此类的方法是线程安全的
 *      API 方法是同步或者异步的。同步方法将阻塞，直到服务器响应为止。异步方法只是将请求排队等待发送并立即返回。
 *      它们采用一个回调对象，该对象将在成功执行请求或者发送错误时执行，并带有只是错误的适当返回码 rc。
 *
 *      一些成功的 ZooKeeper API 调用可以将监视流在 ZooKeeper 服务器中的数据节点上。其他成功的 ZooKeeper API 调用也可以触发这些 Watch。
 *      触发 Watch 后，事件将传递给客户，该客户端将 watch 放在第一位。每个 watch 只能触发一次。因此，每离开一个 watch，最多将有一个事件传给客户
 *
 * 2. Zookeeper 客户端
 *  a. session 会话机制：client 请求和服务端建立连接，服务端会保留和标记当前 client 的 session，包含 session 过期时间，sessionId，然后服务端开始在 session 过期时间
 *      的基础上倒计时，在这段时间内，client 需要向 Server 发送心跳包，目的是让 server 重置 session 过期时间
 *      使用 quit 命令，退出客户端，但是 server 端的 session 不会立即消失，使用 ls / 仍然可以看见创建的临时节点
 *
 *   b. 节点类型
 *      持久节点
 *      临时节点：当客户端断开连接时，这个节点会消失
 *      持久顺序节点：父节点为他的第一级子节点维护一份时序，标记节点的创建顺序，这个标记其实就是一个数字后缀，作为新节点的名字
 *      临时顺序节点，同上，临时节点不能再创建的节点
 *      创建节点时，可以指定每个节点的 data 信息，zookeeper 默认每个节点的数量最大上限是 1M
 *
 * 3. Watcher
 *  a. watch 类型
 *      SyncConnected None(-1) 客户端与服务端建立连接
 *      SyncConnected NodeCreated(1) watcher 监听的数据节点被创建
 *      SyncConnected NodeDeleted(2) watcher 监听的数据节点被删除
 *      SyncConnected NodeDataChanged(3) watch 监听的 node 数据内容发生改变
 *      SyncConnected NodeChildrenChange(4) 被监听的数据节点的节点列表发生变更
 *      Disconnect Node(-1) 客户端与服务端断开连接
 *      Expired Node(-1) 会话超时
 *      AuthFailed Node(-1) 权限校验失败
 *  b. 元数据信息
 *      cZxid: 创建 znode 事务 id
 *      ctime: znode 的创建时间
 *      mZxid: 最后修改的 znode 事务 id
 *      mtime: znode 的最后修改时间
 *      pZxid: 最后修改/删除/添加 znode 的事务 id
 *      cversion: 对当前 znode 子节点的修改次数
 *      dataVersion: 对当前 znode 节点 data 的修改次数
 *      aclVersion: 对 znode 的 acl 修改的次数
 *      ephemeralOwner: 如果 znode 是临时节点，这就是 znode 所有者的 sessionId，否则为 0
 *      dataLength: znode 数据字段的长度
 *      numChildren: znode 子节点数量
 *
 *  c. watch 和 AsyncCallback 的区别
 *      Watcher 用于监听节点的，当节点发生变化时，客户端会收到通知，执行 watcher 的回调
 *      AsyncCallback 是异步方式获取 API 结果，也可以使用同步方式获取
 *
 * 3. Zookeeper 的用途
 *  a. 数据发布与订阅 - 配置中心
 *  b. 负载均衡
 *  c. 命名服务
 *  d. 分布式通知/协调
 *  e. 集群管理与 Master 选举
 *  f. 分布式锁
 *  g. 分布式队列
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/30   14:09
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ZookeeperTest {

    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(1);

    private static final Lock LOCK = new ReentrantLock();

    private static final Condition CALL_BACK = LOCK.newCondition();

    private static final Semaphore SEMAPHORE = new Semaphore(1);

    public static void test() throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.133.134:2181", 10000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                // 获取事件的状态
                Event.KeeperState keeperState = watchedEvent.getState();
                Event.EventType eventType = watchedEvent.getType();
                // 如果是建立连接
                if (Event.KeeperState.SyncConnected == keeperState) {
                    if (Event.EventType.None == eventType) {
                        // 如果建立连接成功，则发送信号量，让后续阻塞程序向下执行
                        System.out.println("zk 建立连接");
                        COUNT_DOWN_LATCH.countDown();
                    }
                }
            }
        });
        COUNT_DOWN_LATCH.await();
        // 创建节点
        String path = zooKeeper.create("/zk_test", Thread.currentThread().getName().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("成功创建节点：" + path);
        // 获取列表
        List<String> children = zooKeeper.getChildren("/", false);
        System.out.println(children);
        // 获取数据
        byte[] data = zooKeeper.getData("/zk_test", false, null);
        System.out.println(new String(data));
        // 删除节点
        zooKeeper.delete("/zk_test", -1);
        // 释放连接
        zooKeeper.close();
    }

    public static void test02() throws IOException, KeeperException, InterruptedException, BrokenBarrierException {

        System.out.println(Thread.currentThread());

        ZooKeeper zooKeeper = new ZooKeeper("192.168.133.134:2181", 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                COUNT_DOWN_LATCH.countDown();
                System.out.println(Thread.currentThread() + ": 连接触发");
            }
        });

        COUNT_DOWN_LATCH.await();

//        Stat stat = new Stat();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        zooKeeper.getData("/node1", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println(Thread.currentThread() + " 回调，事件：" + event);
                if (event.getType().equals(Event.EventType.NodeDataChanged)) {
                    countDownLatch.countDown();
                    System.out.println(Thread.currentThread().getName() + ": 当前节点数据发生了变化");
                }
            }
        }, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                System.out.println(Thread.currentThread());
                System.out.println("rc: " + rc);
                System.out.println("path: " + path);
                System.out.println("data: " + new String(data));
                System.out.println("stat: " + stat);
            }
        }, null);

//        System.out.println(Thread.currentThread().getName() + ": " + content);
        countDownLatch.await();

        TimeUnit.SECONDS.sleep(1);
    }

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException, BrokenBarrierException {
        test02();
    }

}