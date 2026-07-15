package com.microwu.cxd.mybatis.service;

import com.mysql.cj.api.jdbc.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Description:
 *
 * https://blog.csdn.net/baidu_38083619/article/details/83378885
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/4/14   10:31
 * Update History:
 * Author        Time            Content
 */
@Service
public class FileService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Connection connection;

    public int bulkLoadFromInputStream(String loadDataSql, InputStream dataStream) throws SQLException {
        if (dataStream == null) {
            return 0;
        }
        connection = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(loadDataSql);
        int result = 0;
        if (preparedStatement.isWrapperFor(Statement.class)) {
            com.mysql.cj.jdbc.PreparedStatement mysqlStatement = preparedStatement.unwrap(com.mysql.cj.jdbc.PreparedStatement.class);
            mysqlStatement.setLocalInfileInputStream(dataStream);
            result = mysqlStatement.executeUpdate();
        }
        return result;
    }

    public String assembleSql(String dataBaseName, String tableName, String[] columnName) {
//        String insertColumnName = StringUtils.join(columnName, ",");
        String insertColumnName = "";
        String sql = "LOAD DATA LOCAL INFILE 'sql.csv' IGNORE INTO TABLE " + dataBaseName + "." + tableName + "(" + insertColumnName + ")";
        return sql;
    }

    public void builderAppend(StringBuilder builder, Object o) {
        builder.append(o);
        builder.append("\t");
    }

    public void builderEnd(StringBuilder builder, Object o) {
        builder.append(o);
        builder.append('\n');
    }

    public int fastInsertData(String sql, StringBuilder builder) throws IOException, SQLException {
        int rows = 0;
        InputStream is = null;
        try {
            byte[] bytes = builder.toString().getBytes();
            if (bytes.length > 0) {
                is = new ByteArrayInputStream(bytes);
                rows = bulkLoadFromInputStream(sql, is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                is.close();
            }
            if (null != connection) {
                connection.close();
            }
            return rows;
        }
    }

}
