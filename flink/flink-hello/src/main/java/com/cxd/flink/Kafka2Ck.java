package com.cxd.flink;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/7/17   16:06
 * Update History:
 * Author        Time            Content
 */
public class Kafka2Ck {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("bg1:9092")
                .setTopics("test")
                .setGroupId("g1")
                .setStartingOffsets(OffsetsInitializer.latest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

        SingleOutputStreamOperator<WaterSensor> wsStream = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(),
                "kafka-source").map(new WaterSensorMapFunction());

        SinkFunction<WaterSensor> sink = JdbcSink.sink("insert into ws values(?, ?, ?)", new JdbcStatementBuilder<WaterSensor>() {
            @Override
            public void accept(PreparedStatement preparedStatement, WaterSensor waterSensor) throws SQLException {
                preparedStatement.setString(1, waterSensor.getId());
                preparedStatement.setString(2, waterSensor.getTs().toString());
                preparedStatement.setString(3, waterSensor.getVc().toString());
            }
        }, JdbcExecutionOptions.builder()
                .withMaxRetries(3) // 重试次数
                .withBatchSize(100) // 批次的大小：条数
                .withBatchIntervalMs(3000) // 批次的时间
                .build(), new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                .withDriverName("ru.yandex.clickhouse.ClickHouseDriver")
                .withUrl("jdbc:clickhouse://bg1:8123")
                .build());

        wsStream.addSink(sink);

        env.execute();
    }

}
