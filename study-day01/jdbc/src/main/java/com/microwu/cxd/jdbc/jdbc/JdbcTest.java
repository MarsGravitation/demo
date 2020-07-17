package com.microwu.cxd.jdbc.jdbc;

import java.sql.*;

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

    public static void main(String[] args) {
        String url = "";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // 1. 加载并注册 MySQL 的驱动
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            // 2. 根据特定的数据库连接URL，返回与次URL的所匹配的数据库驱动对象
            Driver driver = DriverManager.getDriver(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}