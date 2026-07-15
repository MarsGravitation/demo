package com.cxd.flink.f2;

import com.cxd.flink.domain.WaterSensor;
import com.cxd.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/10/30   13:57
 * Update History:
 * Author        Time            Content
 */
public class KeyedListStateDemo {

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

                            ListState<Integer> vcListState;

                            @Override
                            public void open(Configuration parameters) throws Exception {
                                super.open(parameters);
                                vcListState = getRuntimeContext().getListState(new ListStateDescriptor<Integer>("vcListState", Types.INT));
                            }

                            @Override
                            public void processElement(WaterSensor value, KeyedProcessFunction<String, WaterSensor, String>.Context ctx, Collector<String> out) throws Exception {
                                // 1. 来一条，存到 list 状态里
                                vcListState.add(value.getVc());
                                // 2. 从 list 状态拿出来，卡被盗 list，排序
                                Iterable<Integer> vcListIt = vcListState.get();
                                List<Integer> vcList = new ArrayList<>();
                                for (Integer vc : vcListIt) {
                                    vcList.add(vc);
                                }
                                // 2.2 对 List 进行降序排序
                                vcList.sort((o1, o2) -> o2 - o1);
                                // 2.3 只保留最大的 3 个
                                if (vcList.size() > 3) {
                                    // 将最后一个元素清除
                                    vcList.remove(3);
                                }

                                out.collect("传感器id为" + value.getId() + ",最大的3个水位值=" + vcList);

                                // 3. 更新 list 状态
                                vcListState.update(vcList);
                            }
                        }
                )
                .print();

        env.execute();
    }

}
