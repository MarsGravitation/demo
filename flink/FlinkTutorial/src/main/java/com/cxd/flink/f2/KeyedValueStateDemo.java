package com.cxd.flink.f2;

import com.cxd.flink.domain.WaterSensor;
import com.cxd.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/10/30   13:48
 * Update History:
 * Author        Time            Content
 */
public class KeyedValueStateDemo {

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
                            // 定义状态
                            ValueState<Integer> lastVcState;

                            @Override
                            public void open(Configuration parameters) throws Exception {
                                super.open(parameters);
                                // 在 open 方法中，初始化状态
                                // 状态描述其两个参数：第一个参数，起个名字，不重复
                                // 第二个参数，存储的类型
                                lastVcState = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("lastVcState", Types.INT));
                            }

                            @Override
                            public void processElement(WaterSensor value, KeyedProcessFunction<String, WaterSensor, String>.Context ctx, Collector<String> out) throws Exception {
                                // 1. 取出上一条数据的水位值
                                int lastVc = lastVcState.value() == null ? 0 : lastVcState.value();
                                // 2. 求差值的绝对值，判断是否超过 10
                                Integer vc = value.getVc();
                                if (Math.abs(vc - lastVc) > 10) {
                                    out.collect("传感器=" + value.getId() + "==>当前水位值=" + vc + "，与上一条水位值=" + lastVc + ", 相差超过10！！！");
                                }
                                // 3. 更新状态里的水位值
                                lastVcState.update(vc);
                            }
                        }
                )
                .print();

        env.execute();
    }

}
