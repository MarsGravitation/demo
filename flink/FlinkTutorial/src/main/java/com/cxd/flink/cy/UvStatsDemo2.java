package com.cxd.flink.cy;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Description:
 * 解决数据倾斜问题 --- 有统计的情况下
 *
 * page, uid, time
 * a,1,1
 * a,1,2
 * b,1,1
 * b,2,2
 * a,15,15
 * b,15,15
 *
 * 两阶段聚合法解决 keyBy 算子倾斜
 *
 * 水位线 -> 开窗 -> 聚合 -> 输出
 *
 * 因为是按分钟统计 UV，所以用较大的 HashSet 还是没有瓶颈的。如果窗口更长
 * 或者数据量非常大，就要考虑 HyperLogLog 了
 *
 * 第二个阶段使用 时间戳 keyBy，数据存在 state 中，遍历 state 的数据，输出结果
 *
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/11/7   10:18
 * Update History:
 * Author        Time            Content
 */
public class UvStatsDemo2 {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);
        DataStreamSource<String> sourceStream = env.socketTextStream("master", 7777);

        SingleOutputStreamOperator<UserActionRecord> watermarkedStream = sourceStream.map(new MapFunction<String, UserActionRecord>() {
                    @Override
                    public UserActionRecord map(String value) throws Exception {
                        UserActionRecord record = new UserActionRecord();
                        String[] split = value.split(",");
                        record.setPage(split[0] + "@" + ThreadLocalRandom.current().nextInt(20));
                        record.setUserId(split[1]);
                        record.setTime(Integer.valueOf(split[2]));
                        return record;
                    }
                })
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<UserActionRecord>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                                .withTimestampAssigner(((element, recordTimestamp) -> element.getTime() * 1000L))
                );

        WindowedStream<UserActionRecord, String, TimeWindow> windowedStream = watermarkedStream.keyBy(UserActionRecord::getPage)
                .window(TumblingEventTimeWindows.of(Time.seconds(10)));

        SingleOutputStreamOperator<WindowedViewSum> minutelyPartialAggStream = windowedStream.aggregate(new AggregateFunction<UserActionRecord, ViewAccumulator, ViewAccumulator>() {

            @Override
            public ViewAccumulator createAccumulator() {
                return new ViewAccumulator();
            }

            @Override
            public ViewAccumulator add(UserActionRecord value, ViewAccumulator accumulator) {
                if (accumulator.getKey().isEmpty()) {
                    accumulator.setKey(value.getPage());
                }
                accumulator.addCount(1);
                accumulator.addUserId(value.getUserId());
                return accumulator;
            }

            @Override
            public ViewAccumulator getResult(ViewAccumulator accumulator) {
                return accumulator;
            }

            @Override
            public ViewAccumulator merge(ViewAccumulator a, ViewAccumulator b) {
                return null;
            }
        }, new ProcessWindowFunction<ViewAccumulator, WindowedViewSum, String, TimeWindow>() {
            @Override
            public void process(String s, ProcessWindowFunction<ViewAccumulator, WindowedViewSum, String, TimeWindow>.Context context, Iterable<ViewAccumulator> elements, Collector<WindowedViewSum> out) throws Exception {
                ViewAccumulator accumulator = elements.iterator().next();
                TimeWindow window = context.window();
                String key = accumulator.getKey();
                out.collect(new WindowedViewSum(key.substring(0, key.indexOf("@")), window.getStart(), window.getEnd(), accumulator.getCount(), accumulator.getUserIds()));
            }
        });

        minutelyPartialAggStream.keyBy(WindowedViewSum::getEnd)
                .process(new KeyedProcessFunction<Long, WindowedViewSum, String>() {

                    private ListState<WindowedViewSum> state;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);

                        state = getRuntimeContext().getListState(new ListStateDescriptor<>("state_windowed_pvuv_sum", WindowedViewSum.class));
                    }

                    @Override
                    public void processElement(WindowedViewSum value, KeyedProcessFunction<Long, WindowedViewSum, String>.Context ctx, Collector<String> out) throws Exception {
                        state.add(value);

                        // 这里要多加 1s，等窗口关闭后才进行计算，保证数据全部落完
                        ctx.timerService().registerEventTimeTimer(value.getEnd() + 1);
                    }

                    @Override
                    public void onTimer(long timestamp, KeyedProcessFunction<Long, WindowedViewSum, String>.OnTimerContext ctx, Collector<String> out) throws Exception {
                        super.onTimer(timestamp, ctx, out);

                        Map<String, Tuple2<Integer, Set<String>>> result = new HashMap<>();
                        Long time = 0L;

                        for (WindowedViewSum sum : state.get()) {
                            String page = sum.page();
                            if (!result.containsKey(page)) {
                                result.put(page, new Tuple2<>(0, new HashSet<>(2048)));
                            }
                            Tuple2<Integer, Set<String>> puv = result.get(page);
                            puv.f0 += sum.count();
                            puv.f1.addAll(sum.getUserIds());

                            time = sum.getEnd();
                        }

                        state.clear();
                        for (Map.Entry<String, Tuple2<Integer, Set<String>>> entry : result.entrySet()) {
                            out.collect("time: " + time + ", page: " + entry.getKey() + ",pv: "
                                    + entry.getValue().f0 + ", uv: " + entry.getValue().f1);
                        }
                    }
                })
                .setParallelism(1)
                .print();

        env.execute();
    }

    static class UserActionRecord {

        private String page;
        private String userId;
        private Integer time;

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Integer getTime() {
            return time;
        }

        public void setTime(Integer time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return "UserActionRecord{" +
                    "page='" + page + '\'' +
                    ", userId=" + userId +
                    ", time=" + time +
                    '}';
        }
    }

    static class ViewAccumulator extends Tuple3<String, Integer, Set<String>> {
        public ViewAccumulator() { super("", 0, new HashSet<>(2048)); }

        public ViewAccumulator(String key, int count, Set<String> userIds) { super(key, count, userIds); }

        public String getKey() { return this.f0; }

        public void setKey(String key) { this.f0 = key; }

        public int getCount() { return this.f1; }

        public void addCount(int count) { this.f1 += count; }

        public Set<String> getUserIds() { return this.f2; }

        public void addUserId(String userId) { this.f2.add(userId); }

        public void addUserIds(Set<String> userIds) { this.f2.addAll(userIds); }
    }

    public static class WindowedViewSum extends Tuple5<String, Long, Long, Integer, Set<String>> {

        public WindowedViewSum() { super("", 0L, 0L, 0, new HashSet<>(2048)); }

        public WindowedViewSum(String page, Long start, Long end, Integer count, Set<String> userIds) {
            super(page, start, end, count, userIds);
        }

        public String page() {
            return f0;
        }

        public Long getStart() {
            return f1;
        }

        public Long getEnd() {
            return f2;
        }

        public Integer count() {
            return f3;
        }

        public Set<String> getUserIds() {
            return f4;
        }
    }

}
