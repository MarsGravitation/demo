package com.microwu.cxd.jdbc.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Description:
 *  ObjectPool 就是对象池，提供了 borrowObject 和 ReturnObject 等一系列函数
 *  PooledObject 就是池化对象的封装类，负责记录额外信息，比如说对象状态，对象创建时间等
 *  PooledObjectFactory 是负责管理池化对象生命周期的工厂类，提供 makeObject、destroyObject 等
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/18   9:11
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SimpleJdbcConnectionFactory extends BasePooledObjectFactory<Connection> {

    private static Driver driver;
    private static final String URL = "jdbc:mysql://192.168.133.134:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&useAffectedRows=true&serverTimezone=Asia/Shanghai";
    private String validationQuery = "select 1";
    private boolean defaultAutoCommit;

    static {
        try {
            // 1. 加载并注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 2. 根据 URL 获取指定驱动
            driver = DriverManager.getDriver(URL);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection create() throws Exception {
        // 用于创建池化对象
        Properties properties = new Properties();
        properties.put("user", "root");
        properties.put("password", "123456");
        Connection connect = driver.connect(URL, properties);
        return connect;
    }

    @Override
    public PooledObject<Connection> wrap(Connection connection) {
        // 将池化对象进行封装，返回 DefaultPooledObject
        // 这里也可以返回自己实现的 PooledObject
        return new DefaultPooledObject<>(connection);
    }

    /**
     * 创建池化对象实例，并且使用 PooledObject 进行包装
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/18  9:52
     *
     * @param
     * @return  org.apache.commons.pool2.PooledObject<java.sql.Connection>
     */
    @Override
    public PooledObject<Connection> makeObject() throws Exception {
        return super.makeObject();
    }

    /**
     * 销毁实例，这里调用 close 方法
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/18  9:52
     *
     * @param   	p
     * @return  void
     */
    @Override
    public void destroyObject(PooledObject<Connection> p) throws Exception {
        p.getObject().close();
    }

    /**
     * 校验对象是否安全可用
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/18  9:53
     *
     * @param   	p
     * @return  boolean
     */
    @Override
    public boolean validateObject(PooledObject<Connection> p) {
        try {
            ConnUtil.validateConnection(p.getObject(), this.validationQuery);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 将池返回的对象实例重新初始化
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/18  9:53
     *
     * @param   	p
     * @return  void
     */
    @Override
    public void activateObject(PooledObject<Connection> p) throws Exception {
        Connection connection = p.getObject();
        connection.setAutoCommit(defaultAutoCommit);
    }

    /**
     * 进行反初始化，比如未提交的事务进行 rollback
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/18  9:54
     *
     * @param   	p
     * @return  void
     */
    @Override
    public void passivateObject(PooledObject<Connection> p) throws Exception {
        super.passivateObject(p);
    }
}