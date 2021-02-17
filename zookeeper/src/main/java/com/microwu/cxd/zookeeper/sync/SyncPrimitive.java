package com.microwu.cxd.zookeeper.sync;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Description:
 * 存在的疑点：监视器的开启的时机
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/30   15:09
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SyncPrimitive implements Watcher {

    static ZooKeeper zk = null;
    static Integer mutex;
    String root;

    SyncPrimitive(String address) {
        if (zk == null) {
            try {
                System.out.println("Starting ZK：");
                zk = new ZooKeeper(address, 3000, this);
                mutex = -1;
                System.out.println("Finished starting ZK: " + zk);
            } catch (IOException e) {
                System.out.println(e.toString());
                zk = null;
            }
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        synchronized (mutex) {
            System.out.println("Process: " + watchedEvent.getType());
            mutex.notify();
        }
    }

    public static class Barrier extends SyncPrimitive {
        int size;
        String name;

        Barrier(String address, String root, int size) {
            super(address);
            this.root = root;
            this.size = size;

            if (zk != null) {
                try {
                    zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                } catch (KeeperException e) {
                    System.out.println("Keeper exception when instantiating queue: " + e.toString());
                } catch (InterruptedException e) {
                    System.out.println("Interrupted exception");
                }
            }

            try {
                name = InetAddress.getLocalHost().getCanonicalHostName();
            } catch (UnknownHostException e) {
                System.out.println(e.toString());
            }
        }

        boolean enter() throws KeeperException, InterruptedException {
            zk.create(root + "/" + name, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            while (true) {
                synchronized (mutex) {
                    List<String> list = zk.getChildren(root, true);
                    if (list.size() < size) {
                        mutex.wait();
                    } else {
                        return true;
                    }
                }
            }
        }

        boolean leave() throws KeeperException, InterruptedException {
            zk.delete(root + "/" + name, 0);
            while (true) {
                synchronized (mutex) {
                    List<String> list = zk.getChildren(root, true);
                    if (list.size() > 0) {
                        mutex.wait();
                    } else {
                        return true;
                    }
                }
            }
        }
    }

    public static class Queue extends SyncPrimitive {

        Queue(String address, String name) {
            super(address);
            this.root = name;
            if (zk != null) {
                try {
                    Stat s = zk.exists(root, false);
                    if (s == null) {
                        zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    }
                } catch (KeeperException e) {
                    System.out
                            .println("Keeper exception when instantiating queue: "
                                    + e.toString());
                } catch (InterruptedException e) {
                    System.out.println("Interrupted exception");
                }
            }
        }

        boolean produce(int i) throws KeeperException, InterruptedException {
            ByteBuffer b = ByteBuffer.allocate(4);
            byte[] value;

            b.putInt(i);
            value = b.array();
            zk.create(root + "/element", value, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
            return true;
        }

        int consume() throws KeeperException, InterruptedException {
            int retvalue = -1;
            Stat stat = null;

            // Get the first element available
            while (true) {
                synchronized (mutex) {
                    List<String> list = zk.getChildren(root, true);
                    if (list.size() == 0) {
                        System.out.println("Going to wait");
                        mutex.wait();
                    } else {
                        Integer min = new Integer(list.get(0).substring(7));
                        String minNode = list.get(0);
                        for (String s : list) {
                            Integer tempValue = new Integer(s.substring(7));
                            //System.out.println("Temporary value: " + tempValue);
                            if (tempValue < min) {
                                min = tempValue;
                                minNode = s;
                            }
                        }
                        System.out.println("Temporary value: " + root + "/" + minNode);
                        byte[] b = zk.getData(root + "/" + minNode,
                                false, stat);
                        zk.delete(root + "/" + minNode, 0);
                        ByteBuffer buffer = ByteBuffer.wrap(b);
                        retvalue = buffer.getInt();

                        return retvalue;
                    }
                }
            }
        }
    }
}