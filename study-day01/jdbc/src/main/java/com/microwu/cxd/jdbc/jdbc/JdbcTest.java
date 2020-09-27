package com.microwu.cxd.jdbc.jdbc;

import java.sql.*;
import java.util.Properties;

/**
 * Description:
 *  DriverManger 负责管理数据库驱动程序，根据 URL 获取与之匹配的 Driver 具体实现。
 *  Driver 则负责处理与具体数据库的通信细节，根据 URL 创建数据库连接 Connection。
 *  Connection 表示与数据库的一个连接会话，可以和数据库进行数据交互。
 *  Statement 是需要执行的 SQL 语句或者存储过程语句对应的实体，可以执行对应的 SQL 语句。
 *  ResultSet 是 Statement 执行后获得的结果集对象，可以使用迭代器从中遍历数据。
 *
 * 1. 载入 Driver 实现：
 *  1. Class#forName 作用是要求 JVM 查找并加载指定的类，如果在类中由静态初始化器的话，
 *      JVM 就会执行该类的静态代码段。加载具体 Driver 实现时，就会执行 Driver 中的静态代码段，
 *      将该 Driver 实现注册到 DriverManager。
 *  2. SPI 机制使用 ServiceLoader 类来提供服务发现机制，动态地为某个接口寻找服务实现。当服务的提供者
 *      提供了服务接口的一种实现后，必须根据 SPI 约定在 META-INFO/services 目录下创建一个以服务接口
 *      命名的文件，在该文件中写入实现该服务接口的具体实现类。当服务调用 ServiceLoader 的 load 方法
 *      的时候，ServiceLoader 能够通过约定的目录找到指定的文件，并装载实例化，完成服务发现。
 *
 *      DriverManager 中的 loadInitialDrivers 方法会使用 ServiceLoader 的 load 方法加载目前项目路径下
 *      的所有 Driver 实现。
 *
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/16   14:44
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class JdbcTest {

    private static final String URL = "jdbc:mysql://192.168.133.134:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&useAffectedRows=true&serverTimezone=Asia/Shanghai";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    private static final String SQL = "select * from user";

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // 1. 加载并注册 MySQL 的驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

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