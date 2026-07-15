package com.cxd.flink.f2;

import com.cxd.flink.domain.WaterSensor;
import com.cxd.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.Map;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/10/30   13:57
 * Update History:
 * Author        Time            Content
 */
public class KeyedMapStateDemo {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<WaterSensor> sensorDS = env.socketTextStream("master", 7777)
                .map(new WaterSensorMapFunction())
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                                .withTimestampAssigner((element, recordTimestamp) -> element.getTs() * 1000L)
                );

        sensorDS.keyBy(WaterSensor::getId)
                .process(
                        new KeyedProcessFunction<String, WaterSensor, String>() {

                            MapState<Integer, Integer> vcCountMapState;

                            @Override
                            public void open(Configuration parameters) throws Exception {
                                super.open(parameters);
                                vcCountMapState = getRuntimeContext().getMapState(new MapStateDescriptor<Integer, Integer>("vcCountMapState", Types.INT, Types.INT));
                            }

                            @Override
                            public void processElement(WaterSensor value, KeyedProcessFunction<String, WaterSensor, String>.Context ctx, Collector<String> out) throws Exception {
                                // 1. 判断是否存在 vc 对应的 key
                                Integer vc = value.getVc();
                                if (vcCountMapState.contains(vc)) {
                                    // 1.1 如果包含这个 vc 的 key，直接 + 1
                                    vcCountMapState.put(vc, vcCountMapState.get(vc) + 1);
                                } else {
                                    // 1.2 如果不包含这个 vc 的 key，初始化 1
                                    vcCountMapState.put(vc, 1);
                                }
                                // 2.遍历Map状态，输出每个k-v的值
                                StringBuilder outStr = new StringBuilder();
                                outStr.append("======================================\n");
                                outStr.append("传感器id为" + value.getId() + "\n");
                                for (Map.Entry<Integer, Integer> vcCount : vcCountMapState.entries()) {
                                    outStr.append(vcCount.toString() + "\n");
                                }
                                outStr.append("======================================\n");

                                out.collect(outStr.toString());
                            }
                        }
                )
                .print();

        env.execute();
    }

}
