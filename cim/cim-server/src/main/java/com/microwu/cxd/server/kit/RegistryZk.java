package com.microwu.cxd.server.kit;

import com.microwu.cxd.server.properties.CimServerProperties;
import com.microwu.cxd.server.utils.SpringBeanFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   11:32
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class RegistryZk implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryZk.class);

    private String ip;
    private int port;
    private ZooKeeper zooKeeper;
    private CimServerProperties properties;

    public RegistryZk(String ip, int port) {
        this.ip = ip;
        this.port = port;
        zooKeeper = SpringBeanFactory.getBean(ZooKeeper.class);
        properties = SpringBeanFactory.getBean(CimServerProperties.class);
    }

    @Override
    public void run() {
        // 创建父节点
        try {
            Stat stat = zooKeeper.exists(properties.getZkRoot(), null);
            if (stat == null) {
                zooKeeper.create(properties.getZkRoot(), new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            // 将自己注册到 zk
            String path = properties.getZkRoot() + "/ip-" + ip + ":" + port;
            zooKeeper.create(path, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}