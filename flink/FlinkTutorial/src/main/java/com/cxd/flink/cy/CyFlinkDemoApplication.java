package com.cxd.flink.cy;

/**
 * Description:
 *
 * <a href="https://www.zhihu.com/people/xu-liang-57-86/posts?page=7">...</a>
 *
 * 1. flink 函数
 *  1.1 富函数
 *      open, close, getRuntimeContext
 *
 *  1.2 增量处理函数
 *      ReduceFunction
 *          - 两个输入类型一样：T reduce(T var1, T var2)
 *      AggregateFunction<IN, ACC, OUT>
 *          ACC createAccumulator - 累加器
 *          ACC add(IN var1, ACC var2) - 累加
 *          OUT getResult(ACC var1) - 输出
 *          ACC merge - 合并累加器
 *  1.3 富全量处理函数
 *      ProcessFunction 处理函数，全量缓存，泛型参数就是输入元素和输出元素的类型
 *          processElement - 处理每个元素
 *          onTimer - 计时器
 *          OnTimerContext - 计时器上下文
 *          timerService - 计时器服务
 *          output - 输出
 *
 *  1.4 富窗口全量函数
 *      ProcessWindowFunction 用于分流组，不分组的用 ProcessAllWindowFunction
 *          process
 *          clear
 *          window - 正在处理的窗口对象
 *          currentProcessingTime - 当前处理时间
 *          currentWatermark - 当前水位线
 *          windowState - 每个 key 的窗口状态
 *          globalState - 每个 key 的全局状态
 *          output - 测端输出
 *
 * 2. Timer
 *  最常见的显式利用 Timer 的地方就是 KeyedProcessFunction。processElement 注册 Timer，onTimer 回调
 *
 *  Timer 在窗口机制中也有重要的地位
 *
 *  当 Timer 触发时，实际上是根据时间特征调用 onProcessingTime/onEventTime(Triggerable接口),
 *  进而触发用户函数的 onTimer 回调逻辑
 *
 *  InternalTimerService
 *      每个算子可以有一个或多个 InternalTimerService
 *      InternalTimerService 的四要素是：名称、命名空间类型，键类型，Triggerable 接口
 *
 *  InternalTimeServiceManager、TimerHeapInternalTimer
 *      InternalTimeServiceManager  会用 HashMap 维护一个特定键类型 K 下所有 InternalTimerService
 *      的名称与实例映射。如果名称已存在，直接返回，不会重建
 *      初始化 InternalTimerServiceImpl 时，会同时创建两个包含 TimerHeapInternalTimer 的优先队列，
 *      分别用于维护事件时间和处理时间的 Timer
 *
 *      需要注意的是，Timer 是维护在 JVM 堆内存中的，如果频繁注册大量 Timer，或者同时触发大量 Timer，
 *      也是一笔不小的开销
 *
 *      Timer 的 scope 有两个，一是数据的 key，二是命名空间
 *
 *      如何触发：当 onProcessingTimer 方法被触发回调时，按顺讯从队列中获取到比时间戳 timer 小
 *      的所有 Timer，挨个执行 onTimer 逻辑
 *
 *      事件时间与内部时间戳无关，而与水印有关
 *
 *      当水位线来时，按照调用链流转，触发所有早于水位线的 Timer
 *
 * Checkpoint 机制
 *
 * Flink 数据倾斜问题解决方案
 *  1. 原因
 *      a. 单点问题
 *      b. GC 频繁
 *  2. 数据倾斜定位
 *      a. 定位反压：web ui 监控，获取数据处理瓶颈的 subtask
 *      b. 确定数据倾斜：subtask 接收和发送的数据量
 *  3. 如何处理
 *      a. 并发度设置的比分区数要低：比如kafka，原则上 source 的并发度和 kafka 的分区数一致
 *      b. key 分布不均匀：添加随机前缀打散它们的分布
 *          - 无统计场景（加随机后缀，使用数据到达的序号 - keyBy(uid_rand)）
 *      c. 有统计场景
 *          - 给 key 设置随机数，首先进行局部聚合，清除前缀，然后进行全局的聚合*
 *
 *  背压的产生及 Flink 的反压处理
 *      a. 生产者 无界 buffer 存储，生产者速度大于消费者速度，就会导致 OOM
 *      b. 就算 使用有界 buffer，也不能完全解决问题
 *
 *      Flink 通过有界 buffer 来进行不同 TaskManager 的数据交换。
 *      做法分为静态控流和动态控流
 *
 *      Flink 数据去重
 *
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/11/5   10:51
 * Update History:
 * Author        Time            Content
 */
public class CyFlinkDemoApplication {
}
