package com.cxd.flink;

import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/7/17   15:32
 * Update History:
 * Author        Time            Content
 */
public class ClickHouseUtil {

//    public static <T>SinkFunction<T> getJdbcSink(String sql) {
//        return JdbcSink.<T>sink(sql,
//                new JdbcStatementBuilder<T>() {
//                    @Override
//                    public void accept(PreparedStatement preparedStatement, T t) throws SQLException {
//                        Field[] declaredFields = t.getClass().getDeclaredFields();
//
//                    }
//                })
//    }

}
