package com.cxd.flink.broad;

import com.cxd.flink.domain.WaterSensor;
import com.cxd.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.BroadcastConnectedStream;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/10/18   15:11
 * Update History:
 * Author        Time            Content
 */
public class OperatorBroadcastStateDemo {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);

        // 数据流
        SingleOutputStreamOperator<WaterSensor> sensorDS = env.socketTextStream("master", 7777)
                .map(new WaterSensorMapFunction());

        // 配置流（用来广播配置）
        DataStreamSource<String> configDS = env.socketTextStream("master", 8888);

        // 1. 将配置流广播
        MapStateDescriptor<String, Integer> broadcastMapState = new MapStateDescriptor<>("broadcast-state", Types.STRING, Types.INT);
        BroadcastStream<String> configBS = configDS.broadcast(broadcastMapState);

        // 2. 将 数据流 和 广播后的配置流 connect
        BroadcastConnectedStream<WaterSensor, String> sensorBCS = sensorDS.connect(configBS);

        // 3. 调用 process
        sensorBCS.process(new BroadcastProcessFunction<WaterSensor, String, String>() {
            /**
             * 数据流的处理方式：数据流只能读取广播状态，不能修改
             *
             * @param waterSensor
             * @param readOnlyContext
             * @param collector
             * @throws Exception
             */
            @Override
            public void processElement(WaterSensor waterSensor, BroadcastProcessFunction<WaterSensor, String, String>.ReadOnlyContext readOnlyContext, Collector<String> collector) throws Exception {
                // 5. 通过上下文获取广播状态，取出里面的值
                ReadOnlyBroadcastState<String, Integer> broadcastState = readOnlyContext.getBroadcastState(broadcastMapState);
                Integer threshold = broadcastState.get("threshold");
                // 判断广播状态里是否有数据，因为刚启动时，可能是数据流的第一条数据先来
                threshold = (threshold == null ? 0 : threshold);
                if (waterSensor.getVc() > threshold) {
                    collector.collect(waterSensor + ", 水位超过指定的阈值：" + threshold + "!!!");
                }
            }

            /**
             * 广播后的配置流的处理方法：只有广播流才能修改广播状态
             * @param s
             * @param context
             * @param collector
             * @throws Exception
             */
            @Override
            public void processBroadcastElement(String s, BroadcastProcessFunction<WaterSensor, String, String>.Context context, Collector<String> collector) throws Exception {
                // 4. 通过上下文获取广播状态，往里面写数据
                BroadcastState<String, Integer> broadcastState = context.getBroadcastState(broadcastMapState);
                broadcastState.put("threshold", Integer.valueOf(s));
            }
        }).print();

        env.execute();
    }

}
