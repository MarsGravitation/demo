package com.microwu.cxd.c5

/**
 * 前面的成为父
 * 窄依赖：每个父 RDD 的 partition 最多被子 RDD 的一个 Partition 使用
 *  一对一 or 多对一
 * 宽依赖：同一个父 RDD 的 Partition 被多个子 RDD 的 Partition 依赖
 *  一对多
 *
 */
object TestDep {

}
