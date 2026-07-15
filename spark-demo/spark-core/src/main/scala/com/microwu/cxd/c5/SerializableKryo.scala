package com.microwu.cxd.c5

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * Kryo 速度是 Serializable 的 10 倍。
 * 当 RDD shuffle 数据的时候，简单数据类型、数组和字符串类型已经在 Spark 内部
 * 使用 Kryo 序列化
 *
 * 即使使用 Kryo 序列化，也要继承 Serializable 接口
 */
object SerializableKryo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setAppName("SerDemo")
      .setMaster("local")
      // 替换默认的序列化机制
      .set("spark.serializer",
        "org.apache.spark.serializer.KryoSerializer")
      // 注册需要使用 kryo 序列化的自定义类
      .registerKryoClasses(Array(classOf[Searcher]))

    val sc = new SparkContext(conf)

    val rdd = sc.makeRDD(Array("hello world"), 2)

    val searcher = Searcher("hello")
    val result = searcher.getMatchedRDD1(rdd)

    result.collect().foreach(println)
  }
}

/**
 * 主构造器
 *  如果没有参数，小括号可以省略
 *
 * 构造器参数
 *  没有修饰符，局部变量
 *  var 修饰，成员属性，可以修改
 *  val 修饰，只读属性
 *
 * case class
 *  初始化时不用 new
 *  toString 的实现更漂亮
 *  默认实现了 equals 和 hashCode
 *  默认实现序列化
 *  自动从 scala.Product 继承一些函数
 *  case class 构造函数的参数是 public 级别的，我们可以直接访问
 */
case class Searcher(val query: String) {
  def isMatch(s: String) = {
    s.contains(query)
  }

  def getMatchedRDD1(rdd: RDD[String]) = {
    rdd.filter(isMatch)
  }

  def getMatchedRDD2(rdd: RDD[String]) = {
    var q = query
    rdd.filter(_.contains(q))
  }
}
