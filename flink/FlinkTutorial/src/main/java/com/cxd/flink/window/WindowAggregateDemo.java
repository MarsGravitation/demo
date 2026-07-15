package com.cxd.flink.window;

import com.cxd.flink.domain.WaterSensor;
import com.cxd.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * Description:
 *
 * 1. Keyed 和 Non-Keyed
 *  开窗之前确定是基于 Keyed 还是 Non-Keyed
 *
 *  keyBy 会按照 key 被分成多条逻辑流。这时开窗操作时，窗口计算会在多个并行
 *  子任务上同时执行。所以可以任务，每个 key 上都定义了一组窗口，各自独立地
 *  进行统计计算
 *
 *  Non-Keyed 并行度变成了 1
 *
 *  窗口分配器
 *      时间/计数
 *      滚动/滑动
 *
 *  窗口函数
 *
 *  归约函数 ReduceFunction
 *
 *  聚合函数 AggregateFunction
 *
 *  ReductionFunction 有一个限制，聚合类型，输出结果，输入类型需要一样
 *
 *  AggregateFunction:
 *      1. 调用 createAccumulater 初始化一个状态
 *      2. 来一条数据调用一次 add 方法
 *      3. getResult 得到计算结果
 *
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/7/12   15:12
 * Update History:
 * Author        Time            Content
 */
public class WindowAggregateDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        env.socketTextStream("192.168.139.129", 7777)
                .map(new WaterSensorMapFunction())
                .keyBy(WaterSensor::getId)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
//                .reduce(new ReduceFunction<WaterSensor>() {
//                    @Override
//                    public WaterSensor reduce(WaterSensor value1, WaterSensor value2) throws Exception {
//                        System.out.println("调用 reduce，value1 = " + value1 + ", value = " + value2);
//                        return new WaterSensor(value1.getId(), System.currentTimeMillis(), value1.getVc() + value2.getVc());
//                    }
//                }).print();
                // 输入 中间状态 输出
        .aggregate(new AggregateFunction<WaterSensor, Integer, String>() {
            @Override
            public Integer createAccumulator() {
                System.out.println("创建累加器");
                return 0;
            }

            @Override
            public Integer add(WaterSensor value, Integer accumulator) {
                System.out.println("调用 add 方法，value = " + value);
                return accumulator + value.getVc();
            }

            @Override
            public String getResult(Integer accumulator) {
                System.out.println("调用 getResult 方法");
                return accumulator.toString();
            }

            @Override
            public Integer merge(Integer a, Integer b) {
                System.out.println("调用 merge 方法");
                return null;
            }
        }).print();

        env.execute();
    }
}
