package com.microwu.cxd

import org.apache.spark.{SparkConf, SparkContext}

/*
4.2 核心组件
4.2.1 Driver
  Spark 驱动器节点，用于执行 Spark 任务中的 main 方法，负责实际代码的执行工作。
    |- 将用户程序转换为作业 - job
    |- 在 Executor 之间调度任务 - task
    |- 追踪 Executor 的执行情况
    |- 通过 UI 展示查询运行情况

4.2.2 Executor
  Spark Executor 是集群中工作节点 Worker 中的一个 JVM 进程，负责在 Spark 作业中
中运行具体的任务，任务彼此之间相互独立。Spark 应用启动时，Executor 节点被同时启动，并
始终伴随着整个 Spark 应用的生命周期而存在

4.2.4 ApplicationMaster
  Hadoop 用户想 yarn 集群提交应用程序时，提交程序中应该包含 ApplicationMaster，用于
向资源调度起申请执行任务的资源容器，运行用户自己的程序任务，监控整个任务的运行，跟踪整个任务
的状态，处理任务失败等异常情况。
  ResourceManage 和 Driver 之间的解耦合靠的就是 ApplicationMaster


2.3 Transformation 转换算子
2.3.1 Value 类型
2.3.1.1 map
2.3.1.2 flatMap: 1 -> n，一进迭代器出
2.3.1.3 groupBy
  |- groupBy 会存在 shuffle 过程
  |- shuffle 将不同的分区数据进行打乱重组的过程
  |- shuffle 一定会落盘
2.3.1.4 filter
2.3.1.5 distinct 去重
2.3.1.6 sortBy

2.3.2 K-V类型
2.3.2.1 mapValues
2.3.2.2 groupByKey - 对每个 key 进行操作，只生成一个 seq
2.3.2.3 reduceByKey - 按照 K 聚合 V，分区内 Combiner，分区间 reduceByKey
2.3.2.5 sortByKey

2.4 Action 行动算子
  行动算子是触发了整个作业的执行。因为转换算子都是懒加载，并不会立即执行
2.4.1 collect
2.4.2 count
2.4.3 first
2.4.4 take 返回前 n 个元素
2.3.5 countByKey - 统计每种 key 的个数
2.4.6 save 相关算子
  1) saveAsTextFile 保存成 Text 文件
  2) saveAsObjectFile 序列化成对象保存到文件
2.4.7 foreach 遍历 RDD 中每一个元素
2.4.8 foreachPartition 遍历 RDD 中每一个分区

2.7.2 窄依赖
  每一个父 RDD 的 partition 最多被子 RDD 的一个 Partition 使用，（一对一 or 多对一，独生子女）
2.7.3 宽依赖
  同一个父 RDD 的 partition 被多个子 RDD 的 Partition 依赖，会引起 shuffle
2.7.4 Stage 任务划分
  1)
  2) 一个 action 算子就会生成一个 job
  3) stage 等于宽依赖的个数 + 1
  4) 一个 Stage 阶段中，最后一个 RDD 的分区个数就是 Task 个数
  Application -> Job -> Stage -> Task (1:n)




 */
object SparkCoreDemo02 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("SparkCore")

    val sc = new SparkContext(conf)

    val dataRDD = sc.makeRDD(List(1, 2, 3, 4), 2)

    val groupByRDD = dataRDD.groupBy(_ % 2)

    groupByRDD.collect().foreach(println)

    Thread.sleep(600000)

    sc.stop()

  }
}
