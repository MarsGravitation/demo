package com.cxd.flink.split;

import com.cxd.flink.domain.WaterSensor;
import com.cxd.flink.function.WaterSensorMapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.SideOutputDataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * Description:
 *
 * 分流就是将一条数据流拆分成完全独立的多条流
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/7/12   14:14
 * Update History:
 * Author        Time            Content
 */
public class SplitStreamByFilter {
    public static void main(String[] args) throws Exception {
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

//        SingleOutputStreamOperator<Integer> ds = env.socketTextStream("hadoop102", 7777)
//                .map(Integer::valueOf);
        
//        // 将 ds 分成两个流，一个是奇数流，一个是偶数流
//        // 使用 filter 过滤两次
//
//        // 将原始数据流复制了三份，然后对每一份分别做筛选
//        SingleOutputStreamOperator<Integer> ds1 = ds.filter(x -> x % 2 == 0);
//        SingleOutputStreamOperator<Integer> ds2 = ds.filter(x -> x % 2 == 1);
//
//        ds1.print("奇数");
//        ds2.print("偶数");


        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        SingleOutputStreamOperator<WaterSensor> ds = env.socketTextStream("192.168.139.129", 7777)
                .map(new WaterSensorMapFunction());


        OutputTag<WaterSensor> s1 = new OutputTag<WaterSensor>("s1", Types.POJO(WaterSensor.class)){};
        OutputTag<WaterSensor> s2 = new OutputTag<WaterSensor>("s2", Types.POJO(WaterSensor.class)){};
        //返回的都是主流
        SingleOutputStreamOperator<WaterSensor> ds1 = ds.process(new ProcessFunction<WaterSensor, WaterSensor>()
        {
            @Override
            public void processElement(WaterSensor value, Context ctx, Collector<WaterSensor> out) throws Exception {

                if ("s1".equals(value.getId())) {
                    ctx.output(s1, value);
                } else if ("s2".equals(value.getId())) {
                    ctx.output(s2, value);
                } else {
                    //主流
                    out.collect(value);
                }

            }
        });

        ds1.print("主流，非s1,s2的传感器");
        SideOutputDataStream<WaterSensor> s1DS = ds1.getSideOutput(s1);
        SideOutputDataStream<WaterSensor> s2DS = ds1.getSideOutput(s2);

        s1DS.printToErr("s1");
        s2DS.printToErr("s2");

        env.execute();
    }
}
