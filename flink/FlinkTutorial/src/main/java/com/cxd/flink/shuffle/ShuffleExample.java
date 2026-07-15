package com.cxd.flink.shuffle;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Description:
 *
 * DataStream.shuffle() 将数据随机分配到下游算子的并行任务中去
 *
 * 随机分区后，依然是一个 DataStream
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/7/12   14:07
 * Update History:
 * Author        Time            Content
 */
public class ShuffleExample {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(2);

        // nc -l -p 7777
        DataStreamSource<String> stream = env.socketTextStream("192.168.139.129", 7777);

        stream.shuffle().print();

        env.execute();
    }
}
