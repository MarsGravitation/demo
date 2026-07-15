package com.microwu.cxd

import org.apache.sedona.spark.SedonaContext
import org.apache.sedona.viz.core.Serde.SedonaVizKryoRegistrator
import org.apache.spark.serializer.KryoSerializer
import org.apache.spark.sql.SparkSession

/**
 * 构造函数
 *  ST_MakeEnvelope: 构建多边形
 *
 * Function
 *  GeometryType: 几何类型
 *
 * Predicate 谓词
 *  ST_Contains: 如果 A 完全包含 B，则返回 true
 */
object SedonaApiDemo01 {
  def main(args: Array[String]): Unit = {
    val config = SparkSession.builder()
      .config("spark.serializer", classOf[KryoSerializer].getName).
      config("spark.kryo.registrator", classOf[SedonaVizKryoRegistrator].getName).
      master("local[*]")
      .appName("sedona-api-demo")
      .getOrCreate()

    val sedona = SedonaContext.create(config)

    val df = sedona.sql("SELECT ST_PolygonFromEnvelope(double(1.234),double(2.234),double(3.345),double(3.345))")
    df.show()

    sedona.stop()
  }
}
