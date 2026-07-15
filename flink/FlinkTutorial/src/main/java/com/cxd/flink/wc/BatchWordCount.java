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
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/2/2   10:03
 * Update History:
 * Author        Time            Content
 */
public class BatchWordCount {

    public static void main(String[] args) throws Exception {
        // 1. 创建执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        // 2. 从文件中读取数据 -- 按行读取
        DataSource<String> lineDS = env.readTextFile("D:\\WorkSpace\\demo\\flink\\FlinkTutorial\\src\\main\\resources\\input\\wc.txt");
        // 3. 转换数据格式
        FlatMapOperator<String, Tuple2<String, Integer>> wordAndOne = lineDS.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String line, Collector<Tuple2<String, Integer>> collector) throws Exception {
                String[] split = line.split(",");
                for (String s : split) {
                    collector.collect(Tuple2.of(s, 1));
                }
            }
        });
        // 4. 按照 word 分组
        UnsortedGrouping<Tuple2<String, Integer>> wordAndOneUG = wordAndOne.groupBy(0);
        // 5. 分组内聚合
        AggregateOperator<Tuple2<String, Integer>> sum = wordAndOneUG.sum(1);
        // 6. 打印
        sum.print();
    }

}
