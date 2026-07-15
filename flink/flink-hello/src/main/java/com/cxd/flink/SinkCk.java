package com.cxd.flink;

import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/7/17   15:44
 * Update History:
 * Author        Time            Content
 */
public class SinkCk {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<WaterSensor> sensorDS = env.socketTextStream("master", 7777).map(new WaterSensorMapFunction());

//        SinkFunction<WaterSensor> sink = JdbcSink.sink("insert into ws values(?, ?, ?)", new JdbcStatementBuilder<WaterSensor>() {
//            @Override
//            public void accept(PreparedStatement preparedStatement, WaterSensor waterSensor) throws SQLException {
//                preparedStatement.setString(1, waterSensor.getId());
//                preparedStatement.setString(2, waterSensor.getTs().toString());
//                preparedStatement.setString(3, waterSensor.getVc().toString());
//            }
//        }, JdbcExecutionOptions.builder()
//                .withMaxRetries(3) // 重试次数
//                .withBatchSize(100) // 批次的大小：条数
//                .withBatchIntervalMs(3000) // 批次的时间
//                .build(), new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
//                .withDriverName("ru.yandex.clickhouse.ClickHouseDriver")
//                .withUrl("jdbc:clickhouse://bg1:8123")
//                .build());

        SinkFunction<WaterSensor> sink = JdbcSink.sink("insert into user values(?, ?)", new JdbcStatementBuilder<WaterSensor>() {
            @Override
            public void accept(PreparedStatement preparedStatement, WaterSensor waterSensor) throws SQLException {
                preparedStatement.setObject(1, waterSensor.getId());
                preparedStatement.setObject(2, waterSensor.getTs());
            }
        }, JdbcExecutionOptions.builder()
                .withMaxRetries(3) // 重试次数
                .withBatchSize(100) // 批次的大小：条数
                .withBatchIntervalMs(3000) // 批次的时间
                .build(), new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                .withDriverName("ru.yandex.clickhouse.ClickHouseDriver")
                .withUrl("jdbc:clickhouse://sap:123456789@192.168.12.77:8123/test")
                .build());

        sensorDS.addSink(sink);

        env.execute();
    }

}
