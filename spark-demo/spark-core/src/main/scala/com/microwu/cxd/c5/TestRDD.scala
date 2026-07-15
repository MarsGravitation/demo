package com.microwu.cxd.c5

import org.apache.spark.{SparkConf, SparkContext}

/**
 * RDD: 分布式计算模型
 *  1. 封装了计算逻辑
 *  2. 并行计算
 *
 * Task = 计算逻辑 + 数据
 *
 * 1. 从集合/内存中创建 RDD
 * 2. 从外部存储创建 RDD
 */
class TestRDD {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local").setAppName("RDD")
    val sparkContext = new SparkContext(sparkConf)
    val rdd1 = sparkContext.parallelize(List(1, 2, 3, 4))
    val rdd2 = sparkContext.makeRDD(List(1, 2, 3, 4))

    rdd1.collect().foreach(println)
    rdd2.collect().foreach(println)

    val fileRDD = sparkContext.textFile("input")

    sparkContext.stop()
  }

}
