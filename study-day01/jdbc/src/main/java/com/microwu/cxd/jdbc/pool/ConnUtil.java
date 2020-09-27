package com.microwu.cxd.jdbc.pool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/18   9:34
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ConnUtil {

    public static void validateConnection(Connection connection, String validationQuery) throws SQLException {
        if (connection.isClosed()) {
            throw new SQLException("validateConnection: connection closed");
        }

        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(validationQuery);
            if (!resultSet.next()) {
                throw new SQLException("validationQuery didn't return a row");
            }
        } finally {
            resultSet.close();
            statement.close();
        }

    }
}