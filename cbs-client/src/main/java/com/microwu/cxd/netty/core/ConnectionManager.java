package com.microwu.cxd.netty.core;

import java.util.concurrent.ExecutionException;

/**
 * Description: 连接管理器，对外的门面，包含所有 Connection 对外的操作
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/29   8:28
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public interface ConnectionManager {

    void start();

    boolean add(String uniqueKey, Connection connection);

    Connection get(String uniqueKey) throws ExecutionException, InterruptedException;

    void check(Connection connection) throws Exception;

    Connection getAndCreateIfAbsent(Address address) throws ExecutionException, InterruptedException;

    void shutdown();
}