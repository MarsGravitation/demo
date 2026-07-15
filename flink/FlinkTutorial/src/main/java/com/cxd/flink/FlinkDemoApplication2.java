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
 *          DataStream 无法直接聚合，需要将数据分区，然后聚合
 *          keyBy 将一条流从逻辑上划分成不同的分区
 *          hash(key) % partition
 *
 *      5.3.2.2 简单聚合
 *          sum,min,max,minBy,maxBy
 *
 *          聚合后返回 DataStream
 *
 *          一个聚合算子，会为每一个 key 保存一个聚合的值，叫做 状态。
 *          所以每当有一个新的数据输入，算子就会跟新保存的聚合结果，并
 *          发送一个带有更新后聚合值的事件到下游算子。对于无界流来说，
 *          这些状态是永远不会被清除的，所以我们使用聚合算子，应该只用
 *          在含有有限个 key 的数据流上
 *
 *      5.3.2.3 归约聚合
 *          reduce 可以对已有的数据进行归约处理，把每一个新输入的数据
 *          和当前已归约出来的值，再做一个聚合计算。
 *
 *      5.3.3.2 富函数类
 *          所有的 Flink 函数类都有其 Rich 版本
 *          富函数可以获取运行环境的上下文，并拥有一些生命周期方法
 *
 *      5.3.4 物理分区算子
 *      5.3.4.1 随机分区
 *      5.3.4.2 轮询分区
 *      5.3.4.3 重缩放分区
 *      5.3.4.4 广播
 *      5.3.4.5 全局分区
 *      5.3.4.6 自定义分区
 *
 *      5.3.5 分流
 *      5.3.5.2 使用侧输出流
 *          基于一个DataStream，定义一些筛选条件，将符合条件的数据放到对应的流
 *
 *      5.3.6 合流
 *      5.3.6.1 联合 union
 *      数据类型必须相同
 *
 *      5.3.6.2 连接 connect
 *          map1 对应第一条数据流的 map 操作
 *          map2 对应第二条数据流的 map 操作
 *
 *          keyBy: 就是把两条流中 key 相同的数据放到了一起，产生 inner join 效果
 *
 *  5.4 输出算子
 *
 *  6.1 窗口
 *      滚动窗口，滑动窗口，会话窗口，全局窗口
 *
 *      6.1.3
 *          按键分区和非按键分区
 *          按键分区：stream.keyBy.window
 *          非按键分区：stream.windowAll - 不会分成多条逻辑流，并行度变成 1
 *
 *      6.1.4 窗口分配器
 *      6.1.5 窗口函数
 *      6.1.5.1 增量聚合函数 ReduceFunction/AggregateFunction
 *      ReduceFunction - 类型必须一样
 *      AggregateFunction - 首先 createAccumulator 为任务初始化一个状态
 *      来一条数据调用一次 add，将结果保存到状态中；等到了窗口输出时，调用 getResult
 *      得到计算结果
 *
 *      6.1.5.2 全窗口函数
 *      ProcessWindowFunction
 *
 *      6.1.6 其他 API
 *      6.1.6.1 触发器
 *          触发器主要是用来控制窗口什么时候触发计算。就是执行窗口函数。
 *
 *          <a href="https://zhuanlan.zhihu.com/p/692733686">...</a>
 *
 *          Assigner 会决定数据属于哪个窗口，当窗口的第一个属于它的元素
 *          到达时它被创建，当时间超过窗口的结束时间，窗口会被完全移除，在创建
 *          到移除期间，Trigger 会决定窗口何时被计算
 *
 *          EvenTimerTrigger
 *
 *          CONTINUE: 生么也不做
 *          FIRE: 触发计算
 *          PURGE: 清除窗口元素
 *          FIRE_AND_PURGE: 触发计算，并在之后清除窗口元素
 *
 *          当前 watermark >= window.maxTimestamp fire
 *          onElement() ：当前ctx.watermark>=window.maxTimestamp时FIRE，否则向ctx注册window.maxTimestamp的event-time timer，然后CONTINUE
 *          onEventTime() ：当time==window.maxTimestamp时FIRE，否则CONTINUE
 *          onProcessingTime() ：CONTINUE
 *          onMerge() ：ctx.watermark<window.maxTimestamp()时，向ctx注册window.maxTimestamp timer
 *          clear() ：从ctx删除window.maxTimestamp timer
 *
 *  6.2 时间语义
 *  6.3 水位线
 *      依赖系统时间
 *      自定义逻辑时钟
 *
 *      6.3.4.2 水位线生成策略
 *      a. 负责从流中数据元素的某个字段中提取时间戳，并分配给元素。
 *      时间戳的分配是生成水位线的基础
 *
 *      b. 主要负责按照既定的方式，基于时间戳生成水位线
 *
 *      6.3.4.4 自定义水位线生成器
 *
 *      6.3.5 水位线的传递
 *
 *  6.4 基于时间的合流 --- 双流连接 join
 *
 *  7. 处理函数
 *  7.1.2 ProcessFunction
 *      ProcessFunction extends AbstractRichFunction
 *      泛型：
 *      I: input 输入的数据类型
 *      O: out 处理完成后输出的数据类型
 *
 *      processElement: 每个元素都会调用一次
 *      value: 输入元素
 *      ctx: 上下文
 *      out: 收集器
 *
 *      <a href="https://zhuanlan.zhihu.com/p/361413268">...</a>
 *      onTimer: 注册定时触发器，设定时间到了，执行 onTimer - 只有按键分区才支持
 *
 *      ProcessFunction: DataStream
 *      KeyedProcessFunction: KeyedStream
 *      ProcessWindowFunction: WindowedStream(keyBy)
 *      ProcessAllWindowFunction: AllWindowedStream(没有 KeyBy)
 *      CoProcessFunction
 *      ProcessJoinFunction
 *      BroadcastProcessFunction
 *      KeyedBroadcastProcessProcessFunction
 *
 *  7.2 按键分区处理函数
 *      7.2.1 定时器和定时任务
 *          TimerService 会以 key 和时间戳为标准，对定时器进行去重，也就是说
 *          对于每个 key 和时间戳，最多只有一个定时器
 *
 *      7.3.2 ProcessWindowFunction
 *          处理函数 + 全窗口函数
 *
 *          key: key 分区的字段
 *          context:
 *          elements: 窗口收集的所有数据
 *          out: 发送数据输出计算结果的收集器
 *
 *  8.1 Flink 中的状态
 *      无状态算子: map, filter, flatMap
 *      有状态算子: 出当前数据之外，还需要一些其他数据来得到计算结果
 *
 *      8.1.2 状态的分类
 *      a. 托管状态和原始状态
 *      b. 算子状态和按键分区状态
 *
 *      8.2 按键分区状态
 *      8.2.1 值状态
 *      8.2.2 列表状态
 *      8.2.3 Map 状态
 *          MapState - key、namespace、userKey、userValue
 *          key - keyBy
 *          namespace - 区分窗口，比如小时级别的窗口，key = app1，窗口1= 7点，窗口2=8点
 *      8.2.4 归约状态
 *      8.2.5 聚合状态
 *      8.2.6 状态生存时间 TTL
 *          状态创建时，设置失效时间 = 当前时间 + TTL；如果之后有对状态的访问和修改，
 *          我们可以再对失效时间进行更新；
 *
 *  8.3 算子状态
 *      8.3.1 列表状态
 *      8.3.2 联合列表状态
 *      8.3.3 广播状态
 *  8.4 状态后端
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/2/2   10:03
 * Update History:
 * Author        Time            Content
 */
public class FlinkDemoApplication2 {
}
