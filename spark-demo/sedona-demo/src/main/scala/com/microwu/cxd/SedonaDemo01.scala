package com.microwu.cxd

import org.apache.sedona.spark.SedonaContext
import org.apache.sedona.viz.core.Serde.SedonaVizKryoRegistrator
import org.apache.spark.serializer.KryoSerializer
import org.apache.spark.sql.SparkSession

/**
 * GIS 相关信息：
 * https://www.cnblogs.com/onsummer/p/12081889.html
 * https://e7868a.com/gis-coordinate-project
 *
 * wkt 文件
 * https://blog.csdn.net/cuclife/article/details/140786313
 *
 * PCS|x = f1（GCS|经纬度）
 * PCS|y = f2（GCS|经纬度）
 *
 * GCS：地理坐标系统
 * PCS：投影坐标系统
 *
 * AIO -> 墨卡托坐标系统
 * https://zhuanlan.zhihu.com/p/386249911
 *
 * https://blog.csdn.net/yelangkingwuzuhu/category_10838051_2.html
 * https://blog.csdn.net/yelangkingwuzuhu/article/details/145149784
 * https://blog.csdn.net/yelangkingwuzuhu/article/details/145149095
 *
 * 转换坐标系参考系统
 *  Sedona 不控制坐标系统，可以使用地理坐标系统，也可以使用投影坐标系统
 *
 *  坐标参考系统转换
 *    ST_Transform：
 *
 *  运行空间查询
 *    ST_Contains：
 *      ST_Contains(polygon, point) 点是否在多边形内部，不包括边界
 *    ST_Intersects: 用于判断两个几何对象是否有任何部分相交。
 *      ST_Intersects(polygon, point) 说明要们在内部，要么在边界上
 *    ST_Within：与 ST_Contains 逻辑正好相反
 *
 *  KNN query
 *    ST_Distance: 计算两个几何对象之间的最短距离
 *      SELECT countyname, ST_Distance(ST_PolygonFromEnvelope(1.0,100.0,1000.0,1100.0), newcountyshape) AS distance
 *        FROM spatialdf
 *        ORDER BY distance DESC
 *        LIMIT 5 - 返回给定多边形的 5 个最近邻点
 *
 *  Join Query
 *    Range join: 范围连接
 *      select * from polygondf, pointdf where ST_Contains(x, y)
 *
 *    Distance join: 距离连接，A - B，找到 A - B 的距离小于或等于某个距离
 *      select * from pointdf1, pointdf2 where ST_Distance(x, y) < 2
 *      距离分为平面欧几里得距离和基于米的测地距离计算器
 *
 *  KNN join query
 *    KNN 支持对地理空间数据进行最近邻搜索。根据地理临近性识别给定空间点或区域的 k 个最近邻，
 *    通常使用空间坐标和合适的距离度量
 *
 *    ST_KNN: ST_KNN(R: Table, S: Table, K: Integer, use_spheroid: Boolean)
 *      R 是查询侧表，S 是对象侧表，k 是邻居数量。use_spheroid 是否使用球面距离
 *
 *  DataFrame 和 SpatialRDD 转换
 *    spatialRDD = StructureAdapter.toSpatialRdd(spatialDf, "usacounty")
 *    spatialDf = StructuredAdapter.toDf(spatialRDD, sedona)
 *
 *
 *  读取 CSV 文件
 *    geom_wkt
 *    id,geom_wkt
 *    a,"LINESTRING (2 5, 6 1)"
 *    b,POINT (1 2)
 *    c,"POLYGON ((7 1, 7 3, 9 3, 7 1))"
 *
 *  EWKT: df.withColumn("geometry", ST_SetSRID(col("geometry", 4326)))
 *  id,geom_ewkt
 *  a,"SRID=4326;LINESTRING (2 5, 6 1)"
 *  b,SRID=4326;POINT (1 2)
 *  c,"SRID=4326;POLYGON ((7 1, 7 3, 9 3, 7 1))"
 *
 *  Constructor 构造函数
 *
 *    ST_MakeEnvelope(MinX: Double, MinY: Double, MaxX: Double, MaxY: Double)
 *
 */
object SedonaDemo01 {
  def main(args: Array[String]): Unit = {

    testPredicatePushdownAndRangeJonQuery()

  }

  def hello():Unit = {
    // create sedona config
    val conf = SedonaContext
      .builder()
      .master("local")
      .appName("readTestScala")
      // 如果 SesonaViz 与 SedonaSQL 一起啊使用，需要启用 Sedona Kryo 序列化起
      .config("spark.kryo.registrator", classOf[SedonaVizKryoRegistrator].getName)
      .getOrCreate()

    // initate sedonaContext
    val sedona = SedonaContext.create(conf)
    // 从文本文件加载数据

    // 加载原始 DataFrame
    val rawDf = sedona.read.format("csv")
      .option("delimiter", "\t")
      .option("header", "false")
      .load("input\\usa-county.tsv")

    rawDf.createOrReplaceTempView("rawdf")
    rawDf.show()
  }

  def testPredicatePushdownAndRangeJonQuery():Unit = {
    val sparkSession = SparkSession.builder()
      .config("spark.serializer", classOf[KryoSerializer].getName).
      config("spark.kryo.registrator", classOf[SedonaVizKryoRegistrator].getName).
      master("local[*]")
      .appName("SedonaSQL-demo").getOrCreate()

    val sedona = SedonaContext.create(sparkSession)

    val polygonCsvDf = sparkSession.read.format("csv")
      .option("delimiter", ",")
      .option("header", "false")
      .load("input\\testenvelope.csv")
    polygonCsvDf.createOrReplaceTempView("polygontable")
    polygonCsvDf.show()
    val polygonDf = sparkSession.sql("Select ST_PolygonFromEnvelope(cast(polygontable._c0 as Decimal(24,20)),cast(polygontable._c1 as Decimal(24,20)), cast(polygontable._c2 as Decimal(24,20)), cast(polygontable._c3 as Decimal(24,20))) as polygonshape from polygontable")
    polygonDf.createOrReplaceTempView("polygondf")
    polygonDf.show()

    val pointCsvDF = sparkSession.read.format("csv").option("delimiter",",").option("header","false").load("input\\testpoint.csv")
    pointCsvDF.createOrReplaceTempView("pointtable")
    pointCsvDF.show()
    val pointDf = sparkSession.sql("select ST_Point(cast(pointtable._c0 as Decimal(24,20)),cast(pointtable._c1 as Decimal(24,20))) as pointshape from pointtable")
    pointDf.createOrReplaceTempView("pointdf")
    pointDf.show()

    val rangeJoinDf = sparkSession.sql("select * from polygondf, pointdf where ST_Contains(polygondf.polygonshape,pointdf.pointshape) " +
      "and ST_Contains(ST_PolygonFromEnvelope(1.0,101.0,501.0,601.0), polygondf.polygonshape)")

    rangeJoinDf.explain()
    rangeJoinDf.show(3)
    println(rangeJoinDf.count)
  }
}
