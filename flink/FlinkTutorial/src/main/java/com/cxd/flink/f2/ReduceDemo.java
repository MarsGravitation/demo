package com.cxd.flink.f2;

import com.cxd.flink.domain.WaterSensor;
import com.cxd.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/10/29   14:40
 * Update History:
 * Author        Time            Content
 */
public class ReduceDemo {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.socketTextStream("master", 7777)
                .map(new WaterSensorMapFunction())
                .keyBy(WaterSensor::getId)
                .reduce(new ReduceFunction<WaterSensor>() {
                    @Override
                    public WaterSensor reduce(WaterSensor value1, WaterSensor value2) throws Exception {
                        System.out.println("value1: " + value1);
                        System.out.println("value2: " + value2);

                        int maxVc = Math.max(value1.getVc(), value2.getVc());
                        value1.setVc(maxVc);
                        return value1;
                    }
                })
                .print();

        env.execute();

    }

}
