package com.microwu.cxd.jdbc.pool;

import org.apache.commons.pool2.impl.GenericObjectPool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/18   9:43
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class PoolTest {
    public static void main(String[] args) throws SQLException {
        SimpleJdbcConnectionFactory simpleJdbcConnectionFactory = new SimpleJdbcConnectionFactory();
        GenericObjectPool<Connection> pool = new GenericObjectPool<>(simpleJdbcConnectionFactory);
        pool.setMaxTotal(10);
        DataSource dataSource = new SimpleDataSource(pool);

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from user");
        while(resultSet.next()) {
            System.out.println(resultSet.getString(2));
        }

        resultSet.close();
        statement.close();
        pool.returnObject(connection);

    }
}