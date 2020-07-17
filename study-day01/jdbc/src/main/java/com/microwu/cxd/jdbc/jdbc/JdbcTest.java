package com.microwu.cxd.jdbc.jdbc;

import java.sql.*;
import java.util.Properties;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/16   14:44
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class JdbcTest {

    private static final String URL = "jdbc:mysql://192.168.201.128:3306/test?useUnicode=true";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    private static final String SQL = "select * from user";

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // 1. 加载并注册 MySQL 的驱动
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();

            // 2. 根据特定的数据库连接URL，返回与次URL的所匹配的数据库驱动对象
            Driver driver = DriverManager.getDriver(URL);

            // 3. 传入参数，
            Properties properties = new Properties();
            properties.put("user", USER);
            properties.put("password", PASSWORD);

            // 4. 使用数据库驱动创建数据库连接池 Connection
            connection = driver.connect(URL, properties);

            // 5. 从数据库连接中获得 Statement 对象
            statement = connection.createStatement();

            // 6. 执行 SQL 语句，返回结果
            resultSet = statement.executeQuery(SQL);

            // 7. 处理结果，取出数据
            while (resultSet.next()) {
                System.out.println(resultSet.getString(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 8. 关闭资源，释放资源
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}