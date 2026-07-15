package com.microwu.cxd.c5

import org.apache.spark.SparkContext

object TestAction {

  def main(args: Array[String]): Unit = {
    // Action 行动算子
    // 行动算子是触发了整个作业的执行。因为转换算子都是懒加载，并不会立即执行

    // collection 以数组的形式返回数据集
    // 所有的数据都会被拉取到 driver 端，慎用

    val sc = new SparkContext()

    val rdd = sc.makeRDD(List(1, 2, 3, 4))
    rdd.collect().foreach(println)

    // count 返回个数
    // first 返回第一个元素
    // take 返回前 n 个元素
    // countByKey 统计每种 key 的个数

    // save 相关算子
    // saveAsObjectFile 序列化成对象保存到对象

    // foreach 遍历每一个元素

    // foreachPartition

    sc.stop()
  }

}
