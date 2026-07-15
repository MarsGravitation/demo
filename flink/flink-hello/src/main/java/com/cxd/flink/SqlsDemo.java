package com.cxd.flink;

import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/7/18   15:35
 * Update History:
 * Author        Time            Content
 */
public class SqlsDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

//        String topic = "test";
//        String groupId = "g1";
        tableEnv.executeSql("CREATE TABLE source (\n" +
                "  `user_id` STRING,\n" +
                "  `ts` TIMESTAMP_LTZ(3) METADATA FROM 'timestamp'\n" +
                ") WITH (\n" +
                "  'connector' = 'kafka',\n" +
                "  'topic' = 'quickstart-events',\n" +
                "  'properties.bootstrap.servers' = '192.168.12.77:9092',\n" +
                "  'properties.group.id' = 'g1',\n" +
                "  'scan.startup.mode' = 'latest-offset',\n" +
                "  'format' = 'csv'\n" +
                ")");

        Table table = tableEnv.sqlQuery("select user_id, count(1) c from source group by user_id;");
        tableEnv.createTemporaryView("t1", table);

        Table table1 = tableEnv.sqlQuery("select user_id, c + 1 from t1");
        tableEnv.createTemporaryView("t2", table1);

//        tableEnv.executeSql("insert into sink select * from t1;");

        DataStream<Row> stream = tableEnv.toChangelogStream(table1);
        stream.print();

        env.execute();
    }
}
