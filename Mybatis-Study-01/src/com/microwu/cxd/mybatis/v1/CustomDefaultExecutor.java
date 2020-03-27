package com.microwu.cxd.mybatis.v1;

import java.sql.*;

/**
 * Description: 具体的executor
 * Author:   Administration
 * Date:     2019/2/26 17:59
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class CustomDefaultExecutor implements CustomExecutor {
    @Override
    public <T> T query(String statement, String parameter) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        try{
            conn = getConn();
            ps = conn.prepareStatement(String.format(statement, Integer.parseInt(parameter)));
            ps.execute();
            rs = ps.getResultSet();
            user = new User();
            while(rs.next()){
                user.setId(rs.getInt(1));
                user.setName(rs.getString(2));
                user.setAge(rs.getInt(3));
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }finally{
                    conn = null;
                }
            }
        }
        return (T)user;
    }

    public Connection getConn(){
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://192.168.133.135:3306/test_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&useAffectedRows=true&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "123456";
        Connection conn = null;
        // 加载类驱动
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}