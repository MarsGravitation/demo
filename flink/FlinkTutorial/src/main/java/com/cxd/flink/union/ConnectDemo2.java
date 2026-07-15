package com.cxd.flink.union;

import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/10/18   11:12
 * Update History:
 * Author        Time            Content
 */
public class ConnectDemo2 {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<Integer> source1 = env.socketTextStream("master", 7777).map(Integer::parseInt);

        DataStreamSource<String> source2 = env.socketTextStream("master", 8888);

        /**
         * 1. 一次只能连接两条流
         * 2. 流的数据类型可以不一样
         * 3. 连接后可以调用 mao，flatmap，但是各处理各的
         */
        ConnectedStreams<Integer, String> connect = source1.connect(source2);

        connect.map(new CoMapFunction<Integer, String, String>() {
            @Override
            public String map1(Integer integer) throws Exception {
                return "来自数字流：" + integer;
            }

            @Override
            public String map2(String s) throws Exception {
                return "来自于字母流：" + s;
            }
        }).print();

        env.execute();
    }

}
