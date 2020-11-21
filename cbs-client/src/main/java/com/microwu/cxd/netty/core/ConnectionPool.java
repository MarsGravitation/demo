package com.microwu.cxd.netty.core;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/1   18:14
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ConnectionPool {

    private final Random random;

    private final CopyOnWriteArrayList<Connection> connections;

    public ConnectionPool() {
        this.connections = new CopyOnWriteArrayList<>();
        this.random = new Random();
    }

    public boolean add(Connection connection) {
        if (!connection.isFine()) {
            return false;
        }
        return connections.addIfAbsent(connection);
    }

    public Connection get() {
        if (connections == null || connections.isEmpty()) {
            return null;
        }

        Connection connection = null;
        while (connection == null || !connection.isFine()) {
            connection = connections.get(random.nextInt(connections.size()));
        }
        return connection;
    }

    public void close() {
        for (Connection connection : connections) {
            remove(connection);
        }
        connections.clear();
    }

    private void remove(Connection connection) {
        if (connection == null) {
            return;
        }
        this.connections.remove(connection);
    }
}