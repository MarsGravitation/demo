package com.microwu.cxd

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object TestMethod {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("sparkSql").setMaster("local")

    val sparkSession = SparkSession.builder.config(conf).getOrCreate()

    // 读取 json 文件
    val df = sparkSession.read.json("input/user.txt")
    val sexDf = sparkSession.read.json("input/sex.txt")

    // SQL 风格
    df.createOrReplaceTempView("user")
    sexDf.createOrReplaceTempView("sex")
//    sparkSession.sql("select avg(age) from user").show
    sparkSession.sql("select u.name, s.sex, u.age from user u join sex s on u.name = s.name").show

    sparkSession.stop()
  }

}
