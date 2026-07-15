package com.cxd.flink.cdc;

import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/10/22   10:41
 * Update History:
 * Author        Time            Content
 */
public class FlinkCDCDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

//        // TODO 2. 开启检查点   Flink-CDC将读取binlog的位置信息以状态的方式保存在CK,如果想要做到断点续传,
//        // 需要从Checkpoint或者Savepoint启动程序
//        // 2.1 开启Checkpoint,每隔5秒钟做一次CK  ,并指定CK的一致性语义
//        env.enableCheckpointing(3000L, CheckpointingMode.EXACTLY_ONCE);
//        // 2.2 设置超时时间为 1 分钟
//        env.getCheckpointConfig().setCheckpointTimeout(60 * 1000L);
//        // 2.3 设置两次重启的最小时间间隔
//        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(3000L);
//        // 2.4 设置任务关闭的时候保留最后一次 CK 数据
//        env.getCheckpointConfig().enableExternalizedCheckpoints(
//                CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
//        // 2.5 指定从 CK 自动重启策略
//        env.setRestartStrategy(RestartStrategies.failureRateRestart(
//                3, Time.days(1L), Time.minutes(1L)
//        ));
//        // 2.6 设置状态后端
//        env.setStateBackend(new HashMapStateBackend());
//        env.getCheckpointConfig().setCheckpointStorage(
//                "hdfs://hadoop102:8020/flinkCDC"
//        );
//        // 2.7 设置访问HDFS的用户名
//        System.setProperty("HADOOP_USER_NAME", "atguigu");

        // 创建 Flink-Mysql-CDC 的 Source
        MySqlSource<String> mySqlSource = MySqlSource.<String>builder()
                .hostname("master")
                .port(3306)
                .databaseList("test")
                .tableList("test.user")
                .username("root")
                .password("123456")
                .deserializer(new JsonDebeziumDeserializationSchema())
                .startupOptions(StartupOptions.initial())
                .build();

        // 使用 CDC Source 从 Mysql 读取数据
        DataStreamSource<String> mysqlDS = env.fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "MysqlSource");

        // 打印输出
        mysqlDS.print();

        /**
         * 全量数据
         * {"before":null,"after":{"id":2,"name":"cxj","age":2,"dept_id":1,"create_time":1729004223000},"source":{"version":"1.9.7.Final","connector":"mysql","name":"mysql_binlog_source","ts_ms":0,"snapshot":"false","db":"test","sequence":null,"table":"user","server_id":0,"gtid":null,"file":"","pos":0,"row":0,"thread":null,"query":null},"op":"r","ts_ms":1729566915752,"transaction":null}
         * {"before":null,"after":{"id":1,"name":"cxd","age":1,"dept_id":1,"create_time":1729004223000},"source":{"version":"1.9.7.Final","connector":"mysql","name":"mysql_binlog_source","ts_ms":0,"snapshot":"false","db":"test","sequence":null,"table":"user","server_id":0,"gtid":null,"file":"","pos":0,"row":0,"thread":null,"query":null},"op":"r","ts_ms":1729566915750,"transaction":null}
         *
         * 增量数据
         *
         * {"before":null,"after":{"id":3,"name":"aaa","age":3,"dept_id":1,"create_time":1729595789000},"source":{"version":"1.9.7.Final","connector":"mysql","name":"mysql_binlog_source","ts_ms":1729566989000,"snapshot":"false","db":"test","sequence":null,"table":"user","server_id":1,"gtid":null,"file":"log-bin.000002","pos":381,"row":0,"thread":28,"query":null},"op":"c","ts_ms":1729566988933,"transaction":null}
         *
         * op:
         * c: insert
         * u: update
         * d: delete
         * r: read
         *
         * after
         * before
         */

        // 执行任务
        env.execute();
    }
}
