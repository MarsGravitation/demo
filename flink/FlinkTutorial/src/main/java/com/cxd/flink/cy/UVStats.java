package com.cxd.flink.cy;

import com.cxd.flink.domain.WaterSensor;
import com.cxd.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * Description:
 *
 * page, userId, time
 *
 * globalState
 * 并发度 = 2
 * size: 2
 * uvState: org.apache.flink.runtime.state.heap.HeapValueState@37220278
 * uvState: org.apache.flink.runtime.state.heap.HeapValueState@6e9a8465
 * userMapState: org.apache.flink.runtime.state.UserFacingMapState@500ea69e
 * userMapState: org.apache.flink.runtime.state.UserFacingMapState@7fdf62b7
 *
 * 并发度 = 1
 * size: 1
 * uvState: org.apache.flink.runtime.state.heap.HeapValueState@f17396e
 * userMapState: org.apache.flink.runtime.state.UserFacingMapState@4ec22b6b
 * size: 1
 * uvState: org.apache.flink.runtime.state.heap.HeapValueState@f17396e
 * userMapState: org.apache.flink.runtime.state.UserFacingMapState@3b7bbe93
 *
 *  getRuntimeContext
 * 并行度 = 1
 * uvState: org.apache.flink.runtime.state.heap.HeapValueState@3bb5c0d6
 * userMapState: org.apache.flink.runtime.state.UserFacingMapState@440b18fb
 * uvState: org.apache.flink.runtime.state.heap.HeapValueState@3bb5c0d6
 * userMapState: org.apache.flink.runtime.state.UserFacingMapState@f1ebe27
 *
 * uvState: 14094860
 * userMapState: 557965115
 * uvState: 14094860
 * userMapState: 527412689
 *
 * 并行度=1，时间不同
 * uvState: 774247477
 * userMapState: 390289120
 * uvState: 774247477
 * userMapState: 224191607
 *
 * uvState: 668032588
 * userMapState: 969437503
 * uvState: 668032588
 * userMapState: 875680944
 *
 * 结论：valueState 在 键控流下，跟 key 和 时间窗口有关
 * 在并行度为 1 的时候，
 *
 * 1. keyBy(page)
 * 2. map[userId, bool] 如果 userId 不存在，uv++
 *
 * 并行度 = 1
 * key: s1
 * elements: WaterSensor{id='s1', ts=1, vc=1}|WaterSensor{id='s1', ts=2, vc=2}|
 * windows: 28958528
 * key: s2
 * elements: WaterSensor{id='s2', ts=1, vc=1}|WaterSensor{id='s2', ts=2, vc=2}|
 * windows: 1292123881
 *
 * 并行度为1，s1 水位线到，为什么触发 s2 的计算？ - 多并行度的情况下 watermark对齐会取所有 channel 最小的 watermark。
 * <a href="https://blog.csdn.net/HugeBitter/article/details/118415929">...</a>
 *
 * flink多并行度的情况下，Operator会等所有的subtask的watermark到达之后选取最小的作为最终的waterMark传到下游。
 *
 * <a href="https://www.51cto.com/article/709376.html">...</a>
 * 在 多并行度的情况下，source 的每个 sub task 通常独立生成水印。watermark 通过 operator 时会推进 operators 处的当前 event time，
 * 同时 operators 会为下游生成一个新的 watermark。多并行度的情况下 watermark对齐会取所有 channel 最小的 watermark。
 *
 * <a href="https://zhuanlan.zhihu.com/p/365136566">...</a>
 *
 * watermark 的传递机制
 * 当并行执行的情况下，如下图所示，每次接收的水印发送的水印都是最小的，木桶效应。但
 * 某个分区始终无数据时，就不会更新该分区的 watermark 值，那么窗口就一直不会被触发
 * 计算。这种现象在某些 hash 极端导致数据倾斜很普遍
 *
 * 解决方案：
 * 1. 更改并行度
 * 2. 1.11 支持 watermark 空闲检测
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/11/5   16:53
 * Update History:
 * Author        Time            Content
 */
public class UVStats {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
//
//        // For RocksDBStateBackend
////        RocksDBStateBackend rocksDBStateBackend = new RocksDBStateBackend("hdfs://hostname:8020/yourDir/checkpoint", true);
////        rocksDBStateBackend.enableTtlCompactionFilter();
////        env.setStateBackend(rocksDBStateBackend);
//
        env.socketTextStream("master", 7777)
                .map(new WaterSensorMapFunction())
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                                .withTimestampAssigner((element, recordTimestamp) -> element.getTs() * 1000L)
                )
                .keyBy(WaterSensor::getId)
                .window(TumblingEventTimeWindows.of(Time.seconds(10)))

//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setParallelism(1);
//        SingleOutputStreamOperator<WaterSensor> sensorDS = env
//                .socketTextStream("master", 7777)
//                .map(new WaterSensorMapFunction());
//        WatermarkStrategy<WaterSensor> watermarkStrategy = WatermarkStrategy
//                // 1.1 指定watermark生成：乱序的，等待3s
//                .<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
//                // 1.2 指定 时间戳分配器，从数据中提取
//                .withTimestampAssigner(
//                        (element, recordTimestamp) -> {
//                            // 返回的时间戳，要 毫秒
//                            System.out.println("数据=" + element + ",recordTs=" + recordTimestamp);
//                            return element.getTs() * 1000L;
//                        });
//        SingleOutputStreamOperator<WaterSensor> sensorDSwithWatermark = sensorDS.assignTimestampsAndWatermarks(watermarkStrategy);
//        sensorDSwithWatermark.keyBy(sensor -> sensor.getId())
//                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                .process(new ProcessWindowFunction<WaterSensor, String, String, TimeWindow>() {

                    private ValueState<Long> uvState;
                    private MapState<String, Boolean> userMapState;
//                    private StateTtlConfig ttlConfig;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);

//                        ttlConfig = StateTtlConfig
//                                .newBuilder(org.apache.flink.api.common.time.Time.days(1))
//                                .setUpdateType(StateTtlConfig.UpdateType.OnReadAndWrite)
//                                .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
//                                .build();
                    }

                    @Override
                    public void process(String s, ProcessWindowFunction<WaterSensor, String, String, TimeWindow>.Context context, Iterable<WaterSensor> elements, Collector<String> out) throws Exception {
                        // 因为是统计每天的 uv，所以用窗口时间的日期作为 state name 的一部分，保证同一天的数据使用同一个 state，不同天的数据使用不同的 state
                        long startTime = context.window().getStart();
//                        String day = new SimpleDateFormat("yyyyMMdd").format(new Date(startTime));

                        ValueStateDescriptor<Long> uvDes = new ValueStateDescriptor<>(startTime + "uv", Long.class);
                        MapStateDescriptor<String, Boolean> userMapDes = new MapStateDescriptor<>(startTime + "userMap", String.class, Boolean.class);

                        // 设置 state 的过期时间
//                        uvDes.enableTimeToLive(ttlConfig);
//                        userMapDes.enableTimeToLive(ttlConfig);

                        getRuntimeContext().getState(uvDes);
//                        uvState = context.globalState().getState(uvDes);
//                        userMapState = context.globalState().getMapState(userMapDes);
                        uvState = getRuntimeContext().getState(uvDes);
                        userMapState = getRuntimeContext().getMapState(userMapDes);

                        if (uvState.value() == null) {
                            uvState.update(0L);
                        }

//                        System.out.println("size: " + elements.spliterator().estimateSize());
//                        System.out.println("uvState: " + System.identityHashCode(uvState));
//                        System.out.println("userMapState: " + System.identityHashCode(userMapState));

                        StringBuilder sb = new StringBuilder();
                        for (WaterSensor waterSensor : elements) {
                            sb.append(waterSensor).append("|");
                        }
//                        System.out.println("key: " + s);
//                        System.out.println("elements: " + sb);
//                        System.out.println("windows: " + System.identityHashCode(context.window()));

//                        long uv = uvState.value();
//                        for (WaterSensor waterSensor : elements) {
//
//                        }
                    }
                })
                .print();

        env.execute();
    }

}
