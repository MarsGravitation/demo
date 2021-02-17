package com.microwu.cxd.zookeeper.simple.application;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *  LoadSaidExecutor 是启动入口，他来启动 DataMonitor 监控 zookeeper 节点变化
 *  DataMonitor 负责监控，初次启动和发现变化时，调用 LoadSaidExecutor 的方法来加载最新的数据服务器列表信息
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/1   13:58
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LoadSaIdsExecutor implements Watcher, Runnable, ApplicationDataMonitor.ApplicationDataMonitorListener {

    private ApplicationDataMonitor adm;

    private ZooKeeper zk;

    private static final String ZNODE = "/sas";

    private String hostName = "192.168.133.134:2181";

    public LoadSaIdsExecutor() throws IOException {
        zk = new ZooKeeper(hostName, 300000, this);
        adm = new ApplicationDataMonitor(zk, ZNODE, null, this);
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public void changed(List<String> saIds) {
        new SaIdsLoader(saIds);
    }

    @Override
    public void closing(int rc) {
        synchronized (this) {
            notifyAll();
        }
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                while (!adm.dead) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent event) {
        adm.process(event);
    }

    static class SaIdsLoader extends Thread {
        List<String> saIds;

        public SaIdsLoader(List<String> saIds) {
            this.saIds = saIds;
            start();
        }

        @Override
        public void run() {
            if (saIds != null) {
                saIds.forEach(System.out::println);
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        LoadSaIdsExecutor loadSaIdsExecutor = new LoadSaIdsExecutor();
        TimeUnit.SECONDS.sleep(30);
        loadSaIdsExecutor.run();
    }
}