package com.cxd.flink.transformation;

import com.cxd.flink.domain.WaterSensor;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * Description:
 *
 * flatmap：一般将一个（一般是集合类型）拆分成多个。
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/7/12   11:27
 * Update History:
 * Author        Time            Content
 */
public class TransFlatmap {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<WaterSensor> stream = env.fromElements(
                new WaterSensor("sensor_1", 1L, 1),
                new WaterSensor("sensor_2", 2L, 2),
                new WaterSensor("sensor_2", 2L, 2),
                new WaterSensor("sensor_3", 3L, 3)
        );

        stream.flatMap(new FlatMapFunction<WaterSensor, String>() {
            @Override
            public void flatMap(WaterSensor value, Collector<String> out) throws Exception {
                if (value.id.equals("sensor_1")) {
                    out.collect(String.valueOf(value.vc));
                } else {
                    out.collect(String.valueOf(value.ts));
                    out.collect(String.valueOf(value.vc));
                }
            }
        }).print();

        env.execute();

    }
}
