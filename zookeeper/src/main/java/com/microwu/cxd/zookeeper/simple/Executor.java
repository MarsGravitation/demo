package com.microwu.cxd.zookeeper.simple;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Description:
 * ZooKeeper 应用程序分为两个单元，一个单元维护连接，另一个单元监视数据。在此应用程序中，名为 Executor 的类维护 ZooKeeper 连接，而名为 DataMonitor 的类
 * 监视 ZooKeeper 树中的数据。
 *
 * 需求：
 *  1. 接受如下参数：
 *      a. ZooKeeper 服务地址
 *      b. 被监控的 znode 的名称
 *      c. 可执行命令参数
 *  2. 它会取得 znode 上关联的数据，然后执行命令
 *  3. 如果 znode 变化，客户端重新拉取数据，再次执行命令
 *  4. 如果 znode 小时了，客户端杀掉进行的执行命令
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/27   15:44
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Executor
        implements Watcher, Runnable, DataMonitor.DataMonitorListener
{
    String znode;
    DataMonitor dm;
    ZooKeeper zk;
    String filename;
    String exec[];
    Process child;

    public Executor(String hostPort, String znode, String filename,
                    String exec[]) throws KeeperException, IOException {
        this.filename = filename;
        this.exec = exec;
        zk = new ZooKeeper(hostPort, 3000, this);
        dm = new DataMonitor(zk, znode, null, this);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String hostPort = "192.168.133.134:2181";
        String znode = "/zk_test";
        String filename = "E:\\work-note\\Products\\zookeeper\\test.txt";
        String[] exec = new String[]{"ls"};
        try {
            new Executor(hostPort, znode, filename, exec).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***************************************************************************
     * We do process any events ourselves, we just need to forward them on.
     *
     */
    @Override
    public void process(WatchedEvent event) {
        dm.process(event);
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                while (!dm.dead) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void closing(int rc) {
        synchronized (this) {
            notifyAll();
        }
    }

    static class StreamWriter extends Thread {
        OutputStream os;

        InputStream is;

        StreamWriter(InputStream is, OutputStream os) {
            this.is = is;
            this.os = os;
            start();
        }

        @Override
        public void run() {
            byte b[] = new byte[80];
            int rc;
            try {
                while ((rc = is.read(b)) > 0) {
                    os.write(b, 0, rc);
                }
            } catch (IOException e) {
            }

        }
    }

    @Override
    public void exists(byte[] data) {
        if (data == null) {
            if (child != null) {
                System.out.println("Killing process");
                child.destroy();
                try {
                    child.waitFor();
                } catch (InterruptedException e) {
                }
            }
            child = null;
        } else {
            if (child != null) {
                System.out.println("Stopping child");
                child.destroy();
                try {
                    child.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileOutputStream fos = new FileOutputStream(filename);
                fos.write(data);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                System.out.println("Starting child");
                child = Runtime.getRuntime().exec(exec);
                new StreamWriter(child.getInputStream(), System.out);
                new StreamWriter(child.getErrorStream(), System.err);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}