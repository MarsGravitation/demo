package com.cxd.flink.state;

import com.cxd.flink.domain.WaterSensor;
import com.cxd.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
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
 * Date:       2024/2/2   14:03
 * Update History:
 * Author        Time            Content
 */
public class KeyedValueStateDemo {

    /**
     * 1,1,1
     * 1,1,1
     * 2,1,1
     *
     * lastVcState: org.apache.flink.runtime.state.heap.HeapValueState@289150f8
     * lastVcState: org.apache.flink.runtime.state.heap.HeapValueState@289150f8
     * lastVcState: org.apache.flink.runtime.state.heap.HeapValueState@61f2b8d8
     *
     * 按键分区状态
     *
     * 一个 key 对应一个 state
     *
     * 原理：countState.value()这一步，状态后端会根据当前处理数据的键来返回正确的ValueState中的值。
     *
     * open...
     * open...
     * open...
     * open...
     * open...
     * open...
     * open...
     * open...
     *
     * 算子的生命周期，这里 8 个 open，是并行度为 8
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2024/2/2 14:44
     *
     * @param: args - [java.lang.String]
     * @return: void
     */
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<WaterSensor> sensorDS = env.socketTextStream("master", 7777)
                .map(new WaterSensorMapFunction())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                        .withTimestampAssigner(new SerializableTimestampAssigner<WaterSensor>() {
                            @Override
                            public long extractTimestamp(WaterSensor element, long recordTimestamp) {
                                return element.getTs() * 1000;
                            }
                        }));

        sensorDS.keyBy(WaterSensor::getId).process(new KeyedProcessFunction<String, WaterSensor, String>() {

            // 定义状态
            ValueState<Integer> lastVcState;

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                System.out.println("open...");
                lastVcState = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("lastVcState", Types.INT));
            }

            @Override
            public void processElement(WaterSensor value, KeyedProcessFunction<String, WaterSensor, String>.Context ctx, Collector<String> out) throws Exception {
//                ValueState<Integer> lastVcState = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("lastVcState", Types.INT));
                System.out.println("lastVcState: " + lastVcState.value() + ", address: " + System.identityHashCode(lastVcState));
                // 1. 取出上一条数据的水位值(Integer默认值是null，判断)
                int lastVc = lastVcState.value() == null ? 0 : lastVcState.value();
                // 2. 求差值的绝对值，判断是否超过10
                Integer vc = value.getVc();
                if (Math.abs(vc - lastVc) > 10) {
                    out.collect("传感器=" + value.getId() + "==>当前水位值=" + vc + ",与上一条水位值=" + lastVc + ",相差超过10！！！！");
                }
                // 3. 更新状态里的水位值
                lastVcState.update(vc);
            }
        }).print();

        env.execute();
    }

}
