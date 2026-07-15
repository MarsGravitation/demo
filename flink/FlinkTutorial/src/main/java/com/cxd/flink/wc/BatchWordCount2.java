package com.cxd.flink.wc;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.operators.UnsortedGrouping;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * Description:
 *
 * BatchWordCount 与 Stream 不同
 *
 * 1. 创建执行环境不同
 * 2. 转换处理后，得到的数据对象类型不同
 * 3. keyBy 可以传入一个匿名函数
 * 4. 末尾需要调用 env.execute 开始执行任务
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/7/12   10:27
 * Update History:
 * Author        Time            Content
 */
public class BatchWordCount2 {
    public static void main(String[] args) throws Exception {
        // 1. 创建执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        // 1. 创建流式执行环境
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 2. 从文件读取数据 按行读取（存储的元素就是每行的文本）
        DataSource<String> lineDS = env.readTextFile("D:\\WorkSpace\\demo\\flink\\FlinkTutorial\\src\\main\\resources\\input\\wc.txt");
//        DataStreamSource<String> lintStream = env.readTextFile("");
        // 3. 转换数据格式
        FlatMapOperator<String, Tuple2<String, Long>> wordAndOne = lineDS.flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {
            @Override
            public void flatMap(String line, Collector<Tuple2<String, Long>> out) throws Exception {
                String[] words = line.split(" ");

                for (String word : words) {
                    out.collect(Tuple2.of(word, 1L));
                }
            }
        });
        // 4. 按照 word 进行分组
        UnsortedGrouping<Tuple2<String, Long>> wordAndOneUG = wordAndOne.groupBy(0);
        // 5. 分组内聚合统计
        AggregateOperator<Tuple2<String, Long>> sum = wordAndOneUG.sum(1);
        // 6. 打印结果
        sum.print();
    }
}
