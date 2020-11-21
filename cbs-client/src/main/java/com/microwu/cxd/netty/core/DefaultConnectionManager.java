package com.microwu.cxd.netty.core;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/29   17:39
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class DefaultConnectionManager implements ConnectionManager {

    private final int maxCount = 3;

    private ConnectionFactory connectionFactory;

    private final Map<String, FutureTask<ConnectionPool>> conMap = new ConcurrentHashMap<>();

    public DefaultConnectionManager(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void start() {
        this.connectionFactory.init();
    }

    @Override
    public boolean add(String uniqueKey, Connection connection) {
        return false;
    }

    @Override
    public Connection get(String uniqueKey) throws ExecutionException, InterruptedException {
        return this.conMap.get(uniqueKey).get().get();
    }

    @Override
    public void check(Connection connection) throws Exception {
        if (!connection.isFine()) {
            throw new Exception("connection is not fine");
        }
    }

    @Override
    public Connection getAndCreateIfAbsent(Address address) throws ExecutionException, InterruptedException {
        ConnectionPool pool = this.getConnectionPoolAndCreateIfAbsent(address.uniqueKey(), new FutureTask<ConnectionPool>(() -> {
            ConnectionPool connectionPool = new ConnectionPool();
            for (int i = 0; i < maxCount; i++) {
                Connection connection = this.connectionFactory.create(address);
                connectionPool.add(connection);
            }
            return connectionPool;
        }));
        if (pool == null) {
            return null;
        }
        return pool.get();
    }

    @Override
    public void shutdown() {
        if (this.conMap.isEmpty()) {
            return;
        }

        Iterator<String> iterator = this.conMap.keySet().iterator();
        while(iterator.hasNext()) {
            String uniqueKey = iterator.next();
            this.removeTask(uniqueKey);
            iterator.remove();
        }
    }

    private void removeTask(String uniqueKey) {
        FutureTask<ConnectionPool> task = this.conMap.remove(uniqueKey);
        if (task != null) {
            try {
                ConnectionPool pool = task.get();
                if (pool != null) {
                    pool.close();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private ConnectionPool getConnectionPoolAndCreateIfAbsent(String uniqueKey, FutureTask<ConnectionPool> task) throws ExecutionException, InterruptedException {
        ConnectionPool pool = null;
        while (pool == null) {
            FutureTask<ConnectionPool> futureTask = this.conMap.get(uniqueKey);
            if (futureTask == null) {
                futureTask = this.conMap.putIfAbsent(uniqueKey, task);
                if (futureTask == null) {
                    task.run();
                    futureTask = task;
                }
            }

            pool = futureTask.get();
        }
        return pool;
    }
}