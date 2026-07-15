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
public class TableDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

//        String topic = "test";
//        String groupId = "g1";
        tableEnv.executeSql("CREATE TABLE source (\n" +
                "  `user_id` STRING,\n" +
                "  `item_id` STRING,\n" +
                "  `behavior` STRING,\n" +
                "  `ts` TIMESTAMP_LTZ(3) METADATA FROM 'timestamp'\n" +
                ") WITH (\n" +
                "  'connector' = 'kafka',\n" +
                "  'topic' = 'test',\n" +
                "  'properties.bootstrap.servers' = 'bg1:9092',\n" +
                "  'properties.group.id' = 'g1',\n" +
                "  'scan.startup.mode' = 'latest-offset',\n" +
                "  'format' = 'csv'\n" +
                ")");

//        tableEnv.executeSql("CREATE TABLE sink (\n" +
//                "    id STRING, \n" +
//                "    sumVC BIGINT \n" +
//                ") WITH (\n" +
//                "'connector' = 'print'\n" +
//                ");\n");

        Table table = tableEnv.sqlQuery("select user_id, count(1) from source group by user_id;");
//        tableEnv.createTemporaryView("t1", table);
//
//        tableEnv.executeSql("insert into sink select * from t1;");

//        DataStream<Row> stream = tableEnv.toDataStream(table);
        DataStream<Row> stream = tableEnv.toChangelogStream(table);

        SinkFunction<Row> sink = JdbcSink.sink("insert into ws values(?, ?, ?)", new JdbcStatementBuilder<Row>() {
            @Override
            public void accept(PreparedStatement preparedStatement, Row row) throws SQLException {
                preparedStatement.setString(1, row.getField(0).toString());
                preparedStatement.setString(2, "0");
                preparedStatement.setString(3, row.getField(1).toString());
            }
        }, JdbcExecutionOptions.builder()
                .withMaxRetries(3) // 重试次数
                .withBatchSize(100) // 批次的大小：条数
                .withBatchIntervalMs(3000) // 批次的时间
                .build(), new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                .withDriverName("ru.yandex.clickhouse.ClickHouseDriver")
                .withUrl("jdbc:clickhouse://bg1:8123")
                .build());

        stream.addSink(sink);

        env.execute();
    }
}
