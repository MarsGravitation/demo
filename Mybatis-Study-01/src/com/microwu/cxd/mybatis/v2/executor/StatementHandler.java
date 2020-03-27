package com.microwu.cxd.mybatis.v2.executor;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;

/**
 * Description: 负责查询工作
 * Author:   Administration
 * Date:     2019/2/28 15:11
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class StatementHandler {
    private ResultSetHandler handler = new ResultSetHandler();

    public <T> T query(String statement, String parameter, Class pojo) throws SQLException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        Connection conn = null;
        // TODO:
        statement = String.format(statement, Integer.parseInt(parameter));

        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(statement);
            ps.execute();
            // 查询到这里结束，剩下的交给ResultSetHandlder去做
            return handler.handle(ps.getResultSet(), pojo);
        } finally {
            if(conn != null){
                conn.close();
                conn = null;
            }
        }

    }

    /**
     * @Descrip 获取连接
     * @author 成旭东
     * @date 2019/2/28 15:16
     * @param  * @param
     * @return java.sql.Connection
     */
    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://192.168.133.135:3306/test_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&useAffectedRows=true&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "123456";
        // 加载类驱动
        Class.forName(driver);
        conn = DriverManager.getConnection(url, username, password);
        return conn;
    }
}