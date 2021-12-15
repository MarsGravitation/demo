package com.microwu.cxd.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * https://blog.coderap.com/article/214
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/5  15:43
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class WordCountLocal {

    public static void main(String[] args) {
        // 第一步，创建 SparkConf 对象，设置 Spark 应用的配置信息
        // 使用 setMaster 可以设置 Spark 应用程序要连接的的 Spark 集群的 master 节点的 url
        // 但是如果设置为 local 则代表在本地运行
        SparkConf conf = new SparkConf().setAppName("WordCountLocal").setMaster("local");

        // 第二步，创建 JavaSparkContext 对象
        // 在 Spark 中，SparkContext 是 Spark 所有功能的一个入口
        // 无论是用 java、scala，甚至是 python 编写，都必须有一个 SparkContext
        // 它的主要作用，包括初始化 Spark 应用程序所需的一些核心组件，包括调度器
        // 还会到 Spark Master 节点上进行注册，等等
        // 一句话，Spark
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> lines = sc.textFile("E:\\Users\\issuser\\Workspace\\demo\\study-day01\\spark\\src\\main\\resources\\wordCount.txt");

        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String line) throws Exception {
                return Arrays.asList(line.split(" ")).iterator();
            }
        });

        JavaPairRDD<String, Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String word) throws Exception {
                return new Tuple2<>(word, 1);
            }
        });

        JavaPairRDD<String, Integer> wordCounts = pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });

        wordCounts.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            @Override
            public void call(Tuple2<String, Integer> wordCount) throws Exception {
                System.out.println(wordCount._1 + " appeared " + wordCount._2 + " times");
            }
        });

        sc.close();
    }

}
