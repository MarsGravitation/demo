package com.cxd.flink;

/**
 * Description:
 *
 * 4.2 核心概念
 *
 * 4.2.1 并行度
 * 4.2.2 算子链
 * 4.2.3 任务槽
 *
 * 5.2 源算子
 *
 * 5.3 转换算子
 *  5.3.1 基本转换算子
 *      map,filter,flatMap
 *  5.3.2 聚合算子
 *      5.3.2.1 按键分区 keyBy
 *      5.3.2.2 简单聚合 sum,min,max,minBy,maxBy
 *          一个聚合算子，会为每一个 key 保存一个聚合的值，在 Flink 中叫做 状态。
 *          所以每当有一个新的数据输入，算子就会更新保存的聚合结果，并发送一个带有
 *          更新后聚合值的事件到下游算子。对于无界流来说，这个状态是永远不会被清除的，
 *          所以我们使用聚合算子，应该只用在含有有限个 key 的数据流上
 *      5.3.2.3 归约聚合 reduce
 *          对已有的数据进行归约，把每一个新输入的数据和当前已经归约出来的数据，做一个聚合计算
 *      5.3.3.2 富函数
 *          所有的 Flink 都有 Rick 版本。
 *          含有 open, close 生命周期方法
 *
 *  5.3.4 物理分区算子（分区是将数据分配到下游算子的并行任务中取）
 *      5.3.4.1 随机分区 shuffle
 *      5.3.4.2 轮询分区
 *      5.3.4.3 重缩放分区
 *      5.3.4.4 广播
 *      5.3.4.5 全局分区
 *      5.3.4.6 自定义分区
 *
 *  5.3.5 分流（分流是将数据流拆分成多条流）
 *      5.2.5.2 侧输出流
 *
 *  5.3.6 合流
 *      5.3.6.1 联合 union 数据类型必须相同
 *      5.3.6.2 连接
 *
 * 5.4 输出算子
 *
 * 6.1 窗口
 *
 * 6.1.3 窗口 API
 *  keyBy 被分成多条逻辑流。可以认为，每个 key 上都定义了一组窗口，各自独立地进行统计计算
 * 6.1.4 窗口分配器
 * 6.1.5 窗口函数
 *  6.1.5.1 增量聚合函数 ReduceFunction/AggregateFunction
 *      归约函数 ReduceFunction
 *      聚合函数 AggregateFunction
 *  6.1.5.2 全窗口函数 - 先收集窗口中的数据，在内部缓存起来，然后再计算输出
 *      ProcessWindowFunction
 *
 * 6.2 时间语义
 *
 * 6.4 基于时间的合流 --- 双流联结
 *
 * 7.1 基本处理函数
 *  水位线 + 富函数 + 状态
 *
 *  processElement：每个元素都会调用一次
 *      - value: 流总的输入元素
 *      - ctx：上下文，可以获取当前时间戳，查询时间和注册定时器的定时任务，侧输出流
 *      - out：输出数据
 *  onTimer：如果注册了定时任务，当时间到了会执行 onTimer 方法
 *      - 时间戳
 *      - 上下文：
 *      - 收集器（out）
 *   只有按键分区流才支持设置定时器的操作
 *
 *  7.1.3 处理函数分类
 *      ProcessFunction：DataStream
 *      KeyedProcessFunction：KeyedStream
 *      ProcessWindowFunction：WindowedStream - keyBy
 *      ProcessAllWindowFunction：AllWindowedStream - all
 *      CoProcessFunction：ConnectedStream
 *      ProcessJoinFunction：IntervalJoined
 *      BroadcastProcessFunction：DataStream, BroadcastStream connect
 *      KeyedBroadcastProcessFunc：KeyedStream BroadcastStream connect
 *
 *  7.5 侧输出流
 *
 *  8.1 Flink中的状态
 *      无状态算子：map，filter，flatMap
 *      有状态算子：算子状态、按键分区状态
 *
 *      算子状态：一个并行的子任务
 *      按键分区状态：key
 *
 *      也可以通过复函数类来定义 Keyed State。
 *
 *      Flink 中所有的算子都可以是有状态的。
 *      无论是 Keyed State 还是 Operator State，它们都是在本地实例上维护的，
 *      也就是说每个并行子任务维护着对应的状态，算子的子任务之间状态不共享
 *
 *  8.2 按键分区状态
 *  8.2.1 值状态
 *  8.2.2 列表状态
 *  8.2.3 Map 状态
 *  8.2.4 归约状态
 *  8.2.5 聚合状态
 *  8.2.6 状态生存时间 TTL
 *
 *  8.3 算子状态
 *      作用范围被限定为当前算子
 *
 *  8.3.1 列表状态
 *  8.3.2 联合列表状态
 *  8.3.3 广播状态
 *
 *  8.4 状态后端
 *
 *  9 容错机制
 *
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/2/2   10:03
 * Update History:
 * Author        Time            Content
 */
public class FlinkDemoApplication {
}
