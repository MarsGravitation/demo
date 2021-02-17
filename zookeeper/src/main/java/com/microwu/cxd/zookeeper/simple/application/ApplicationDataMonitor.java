package com.microwu.cxd.zookeeper.simple.application;

import org.apache.zookeeper.*;

import java.util.List;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/1   13:45
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ApplicationDataMonitor implements Watcher, AsyncCallback.ChildrenCallback {

    ZooKeeper zk;

    String znode;

    Watcher chainedWatcher;

    boolean dead;

    ApplicationDataMonitorListener listener;

    List<String> prevSaIds;

    public ApplicationDataMonitor(ZooKeeper zk, String znode, Watcher chainedWatcher, ApplicationDataMonitorListener listener) {
        this.zk = zk;
        this.znode = znode;
        this.chainedWatcher = chainedWatcher;
        this.listener = listener;
        // 这是整个监控的开始，通过获取 children 节点开始，设置了本对象为监控对象，回调对象也是本对象，以后均是事件驱动
        zk.getChildren(znode, true, this, null);
    }

    public interface ApplicationDataMonitorListener {
        void changed(List<String> saIds);

        void closing(int rc);
    }

    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children) {
        boolean exists;
        switch (rc) {
            case KeeperException.Code.Ok:
                exists = true;
                break;
            case KeeperException.Code.NoNode:
                exists = false;
                break;
            case KeeperException.Code.SessionExpired:
            case KeeperException.Code.NoAuth:
                dead = true;
                listener.closing(rc);
                return;
            default:
                // Retry errors
                zk.getChildren(znode, true, this, null);
                return;
        }

        List<String> saIds = null;

        // 如果存在，再次查询最新 children，此时仅查询，不要设置监控了
        if (exists) {
            try {
                saIds = zk.getChildren(znode, null);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }

        //拿到最新saids后，通过listener（executor），加载Saids。
        if ((saIds == null && saIds != prevSaIds) || (saIds != null && !saIds.equals(prevSaIds))) {
            listener.changed(saIds);
            prevSaIds = saIds;
        }
    }

    /**
     * 监控 saids 的回调函数，除了处理异常外
     * 如果发生变化，和构造函数中一样，通过 getChildren，再次监控，并处理 children 节点变化后的业务
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/1  14:21
     *
     * @param   	event
     * @return  void
     */
    @Override
    public void process(WatchedEvent event) {
        String path = event.getPath();
        if (event.getType() == Event.EventType.None) {
            switch (event.getState()) {
                case SyncConnected:
                    break;
                case Expired:
                    dead = true;
                    listener.closing(KeeperException.Code.SESSIONEXPIRED.intValue());
                    break;
            }
        } else {
            if (path != null && path.equals(znode)) {
                zk.getChildren(znode, true, this, null);
            }
        }

        if (chainedWatcher != null) {
            chainedWatcher.process(event);
        }
    }
}