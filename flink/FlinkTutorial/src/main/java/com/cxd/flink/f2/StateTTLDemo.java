package com.cxd.flink.f2;

import com.cxd.flink.domain.WaterSensor;
import com.cxd.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * Description: 生存状态时间 TTL
 *  实际应用中，很多状态会随着时间的推移逐渐增长，如果不加以限制，最终就回导致
 *  存储空间的耗尽。一个优化的思路是直接在代码中调用 clear 方法去清除状态。
 *
 *  ttl：给状态附加一个属性。状态创建的时候，设置 失效时间 = 当前时间 + TTL，
 *  之后如果有对状态的访问和修改，我们可以再对失效时间进行更新；当设置的清除条件被
 *  触发时，判断状态是否失效，从而进行清除
 *
 *  TTL 只支持处理时间
 *
 *
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/11/5   16:13
 * Update History:
 * Author        Time            Content
 */
public class StateTTLDemo {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<WaterSensor> sensorDS = env.socketTextStream("master", 7777)
                .map(new WaterSensorMapFunction())
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                                .withTimestampAssigner(((element, recordTimestamp) -> element.getTs() * 1000L))
                );

        sensorDS.keyBy(WaterSensor::getId)
                .process(new KeyedProcessFunction<String, WaterSensor, String>() {

                    ValueState<Integer> lastVcState;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        System.out.println("open.....");

                        // 创建 StateTtlConfig
                        StateTtlConfig stateTtlConfig = StateTtlConfig
                                .newBuilder(Time.seconds(5))
//                                .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
                                .setUpdateType(StateTtlConfig.UpdateType.OnReadAndWrite)
                                .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
                                .build();

                        // 状态描述器 启用 TTL
                        ValueStateDescriptor<Integer> stateDescriptor = new ValueStateDescriptor<>("lastVcState", Types.INT);
                        stateDescriptor.enableTimeToLive(stateTtlConfig);

                        this.lastVcState = getRuntimeContext().getState(stateDescriptor);
                    }

                    @Override
                    public void processElement(WaterSensor value, KeyedProcessFunction<String, WaterSensor, String>.Context ctx, Collector<String> out) throws Exception {
                        Integer lastVc = lastVcState.value();
                        out.collect("key=" + value.getId() + ",状态值=" + lastVc);
                        if (value.getVc() > 10) {
                            lastVcState.update(value.getVc());
//                            System.out.println("lastVc: " + lastVcState.value() + ", vc: " + value.getVc());
                        }
                    }
                })
                .print();

        env.execute();
    }

}
