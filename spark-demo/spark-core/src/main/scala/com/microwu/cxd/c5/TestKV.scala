package com.microwu.cxd.c5

import org.apache.spark.SparkContext

object TestKV {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext()

    val rdd1 = sc.parallelize(List((1, 2)))

    // mapValues 只对 V 进行操作
    val rdd2 = rdd1.mapValues(_ + "|")

    // groupByKey 按照 k 分组
    // (a,1) (a,2) -> (a, [1,2])
    // 只生成一个 seq，不聚合
    val rdd3 = rdd1.groupByKey()

    // reduceByKey 按照 k 聚合 v
    val rdd4 = rdd1.reduceByKey(_ + _)

    // reduceByKey: 按照 key 进行聚合，在 shuffle 之前有 combine 预聚合操作，返回 RDD[K,V]
    // 预聚合：在本分区先进行 聚合操作，即 _+_，然后 shuffle
    // groupByKey: 按照 key 进行分组，直接进行 shuffle

    // sortByKey 按照 k 进行排序
    // k 必须实现 ordered 接口
    val rdd5 = rdd1.sortByKey(true)

    sc.stop()
  }
}
