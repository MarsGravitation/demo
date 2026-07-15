package com.microwu.cxd

import org.apache.spark.{SparkConf, SparkContext}

/**
 * hadoop 环境
 * https://blog.csdn.net/weixin_44628096/article/details/136666271
 */
object WordCount {
  def main(args: Array[String]): Unit = {
    // 创建 spark 运行配置对象
    val sparkConf = new SparkConf().setMaster("local").setAppName("WordCount")
    // 创建 spark 上下文环境对象
    val sc = new SparkContext(sparkConf)
    // 读取文件数据
    val fileRDD = sc.textFile("input/word.txt")
    // 将文件中的数据进行分词
    val wordRDD = fileRDD.flatMap(_.split(" "))
    // 转换数据结构
    val word2OneRDD = wordRDD.map((_, 1))
    // 分组聚合
//    word2OneRDD.reduceByKey((x, y) => {x + y})
//    word2OneRDD.reduceByKey((x, y) => x + y)
    val word2Count = word2OneRDD.reduceByKey(_ + _)
    // 打印结果
    word2Count.foreach(println)
    // 关闭连接
    sc.stop()
  }
}
