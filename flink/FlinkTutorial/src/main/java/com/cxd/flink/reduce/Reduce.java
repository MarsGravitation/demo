package com.cxd.flink.reduce;

import com.cxd.flink.domain.WaterSensor;
import com.cxd.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/10/18   10:53
 * Update History:
 * Author        Time            Content
 */
public class Reduce {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.socketTextStream("master", 7777)
                .map(new WaterSensorMapFunction())
                .keyBy(WaterSensor::getId)
                .reduce(new ReduceFunction<WaterSensor>() {
                    @Override
                    public WaterSensor reduce(WaterSensor value1, WaterSensor value2) throws Exception {
                        System.out.println("Demo7_Reduce.reduce");

                        int maxVc = Math.max(value1.getVc(), value2.getVc());
                        // 实现 max(vc) 的效果，取最大值，其他字段以当前组的第一个为主
                        value1.setVc(maxVc);
                        return value1;
                    }
                })
                .print();

        env.execute();
    }

}
