package com.cxd.flink.aggregation;

import com.cxd.flink.domain.WaterSensor;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Description:
 *
 * 一般来说聚合之前先进行分区
 *
 * keyBy -> aggregation
 *
 * keyBy 将一条流从逻辑上划分成不同的分区。这里的分区就是并行处理的子任务。
 *
 * 通过 key 的 hash code 对分区进行取模运算
 *
 * 简单聚合
 *  sum/min/max/minBy/maxBy
 *
 * keyBy 和 聚合是成对出现的，先分区、后聚合，得到的是 DataStream
 *
 * 一个聚合算子，会为每一个 key 保存一个聚合的值（state）。对于无界流，只能用在
 * 有限个 key 的数据流上
 *
 * 规约聚合 --- reduce
 *
 * keyedStream->DataStream
 *
 * public interface ReduceFunction<T> extends Function, Serializable {
 *      T reduce(T value1, T value2) throws Exception;
 * }
 *
 * 接收两个输入事件，经过转换处理后输出一个相同类型的事件。
 *
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/7/12   11:43
 * Update History:
 * Author        Time            Content
 */
public class TransKeyBy {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<WaterSensor> stream = env.fromElements(
                new WaterSensor("sensor_1", 1L, 1),
                new WaterSensor("sensor_1", 2L, 2),
                new WaterSensor("sensor_2", 2L, 2),
                new WaterSensor("sensor_3", 3L, 3)
        );

//        stream.keyBy(WaterSensor::getId).print();

        // 实现 max 和 maxBy 功能
        stream.keyBy(WaterSensor::getId)
                        .reduce(new ReduceFunction<WaterSensor>() {
                            @Override
                            public WaterSensor reduce(WaterSensor value1, WaterSensor value2) throws Exception {
                                System.out.println("reduce...");

                                // reduce 也要对每一个 key 保存状态
                                int maxVc = Math.max(value1.getVc(), value2.getVc());
                                // 实现 max(vc) 效果 取最大值，其他字段以挡圈组的第一个为主
//                                value1.setVc(maxVc);

                                // 实现 maxBy(vc) 的效果，取当前最大值的所有字段
                                if (value1.getVc() > value2.getVc()) {
                                    value1.setVc(maxVc);
                                    return value1;
                                } else {
                                    value1.setVc(maxVc);
                                    return value2;
                                }
//                                return null;
                            }
                        })
                                .print();

        env.execute();
    }

}
