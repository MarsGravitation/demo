package com.microwu.cxd.c5

import org.apache.spark.SparkContext

/**
 * Value 类型
 */
object TestValue {
  def main(args: Array[String]): Unit = {
    // map：将处理的数据逐条进行映射转换
    val sc = new SparkContext()
    val dataRDD = sc.makeRDD(List(1, 2, 3, 4))
    val dataRDD1 = dataRDD.map(
      num => {
        num * 2
      }
    )

    // flatMap: f 函数的返回值是一个集合，并且将集合中的元素拆分出来放到新的 RDD 中
    // 一进、迭代器出
    val dataRDD2 = sc.makeRDD(List(List(1, 2), List(3, 4)))
    dataRDD2.flatMap(list => list)

    // groupBy 分组
    // groupBy 会存在 shuffle 过程
    // shuffle：将不同的分区数据进行打乱重组的过程
    // shuffle 一定会落盘
    val dataRDD3 = sc.makeRDD(List(1, 2, 3, 4))
    val dataRDD4 = dataRDD3.groupBy(_ % 2)

    // filter
    val dataRDD5 = dataRDD3.filter(_%2==0)

    // distinct
    val dataRDD6 = dataRDD5.distinct()

    // sortBy 排序
    // 1. 排序标准
    // 2. true 正序
    // 3. 排序之后的分区个数
    val dataRDD7 = dataRDD6.sortBy(num => num, false, 4)
  }
}
