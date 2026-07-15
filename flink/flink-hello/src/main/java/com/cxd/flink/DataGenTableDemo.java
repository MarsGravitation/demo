package com.cxd.flink;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/10/22   15:07
 * Update History:
 * Author        Time            Content
 */
public class DataGenTableDemo {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        tableEnv.executeSql("CREATE TABLE datagen (\n" +
                " f_sequence INT,\n" +
                " f_random INT,\n" +
                " f_random_str STRING,\n" +
                " ts AS localtimestamp,\n" +
                " WATERMARK FOR ts AS ts\n" +
                ") WITH (\n" +
                " 'connector' = 'datagen',\n" +
                "\n" +
                " -- optional options --\n" +
                "\n" +
                " 'rows-per-second'='1',\n" +
                "\n" +
                " 'fields.f_sequence.kind'='sequence',\n" +
                " 'fields.f_sequence.start'='1',\n" +
                " 'fields.f_sequence.end'='10',\n" +
                "\n" +
                " 'fields.f_random.min'='1',\n" +
                " 'fields.f_random.max'='1000',\n" +
                "\n" +
                " 'fields.f_random_str.length'='10'\n" +
                ")");

        /**
         * +I[1, 343, c0ec7cd661, 2024-10-22T16:35:08.665]
         * +I[2, 309, 48b2eb3e07, 2024-10-22T16:35:09.650]
         * +I[3, 294, 5ce1788513, 2024-10-22T16:35:10.649]
         */
//        Table table = tableEnv.sqlQuery("select * from datagen");
//        tableEnv.toDataStream(table).print();

        /**
         * +I[1, 1]
         * +I[2, 1]
         * +I[3, 1]
         * +I[4, 1]
         * +I[5, 1]
         */
        Table table = tableEnv.sqlQuery("select f_sequence, count(1) from datagen group by f_sequence");
        tableEnv.toChangelogStream(table).print();

        env.execute();
    }

}
