package com.cxd.flink.f2;

import com.cxd.flink.domain.WaterSensor;
import com.cxd.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.TimerService;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/10/29   16:39
 * Update History:
 * Author        Time            Content
 */
public class KeyedProcessTimerDemo {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<WaterSensor> sensorDS = env.socketTextStream("master", 7777)
                .map(new WaterSensorMapFunction())
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                                .withTimestampAssigner(((element, recordTimestamp) -> {
                                    return element.getTs() * 1000L;
                                }))
                );

        KeyedStream<WaterSensor, String> sensorKS = sensorDS.keyBy(WaterSensor::getId);

        sensorKS.process(new KeyedProcessFunction<String, WaterSensor, String>() {
            @Override
            public void processElement(WaterSensor value, KeyedProcessFunction<String, WaterSensor, String>.Context ctx, Collector<String> out) throws Exception {
                // 获取当前数据的 key
                String currentKey = ctx.getCurrentKey();

                // 定时器注册
                TimerService timerService = ctx.timerService();
                // 1. 事件时间的案例
                // 从数据中提取出来的事件时间
                Long currentEventTime = ctx.timestamp();
                timerService.registerEventTimeTimer(5000L);
                System.out.println("当前 key = " + currentKey + "，当前时间=" + currentEventTime + ", 注册了一个 5s 的定时器");
            }

            @Override
            public void onTimer(long timestamp, KeyedProcessFunction<String, WaterSensor, String>.OnTimerContext ctx, Collector<String> out) throws Exception {
                super.onTimer(timestamp, ctx, out);
                String currentKey = ctx.getCurrentKey();

                System.out.println("key=" + currentKey + "现在时间是" + timestamp + "定时器触发");
            }
        }).print();

        env.execute();
    }

}
