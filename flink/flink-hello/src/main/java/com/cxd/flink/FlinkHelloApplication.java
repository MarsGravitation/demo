package com.cxd.flink;

/**
 * Description:
 *
 * 10.2 流处理中的表
 *  10.2.1 动态表和持续查询
 *      当流中有新数据到来，初始的表会插入一行；而基于这个表定义的 SQL 查询，就应该更新
 *      由于每次数据到来都会触发查询操作
 *
 *      a. 流被转换成动态表
 *      b. 对动态表进行持续查询，生成新的动态表
 *      c. 生成的动态表被转换成流
 *  10.2.2 将流转换成动态表
 *
 * 10.3 时间属性
 *  10.3.1 事件属性
 *      WATERMARK FOR ts AS ts - INTERVAL '5' SECOND
 *  10.3.2 处理时间
 *      ts AS PROCTIME()
 *
 *  10.4.2 表
 *      a. 物理列
 *      b. 元数据列
 *          record_time TIMESTAMP_LTZ(3) METADATA FROM 'timestamp'
 *
 *      有界无序: WATERMARK FOR rowtime_column AS rowtime_column - INTERVAL 'string' timeUnit
 *      生成的是运行 5s 延迟的 Watermark
 *
 *      c. with 指定外部存储系统的元数据信息
 *
 *  10.5.1 With 子句
 *      公共表达式
 *
 *      WITH source_with_total AS (
 *          SELECT id, vc+10 AS total
 *          FROM source
 *      )
 *
 *      select id, sum(total)
 *      FROM source_with_total
 *      group by id;
 *
 *  10.5.4 分组聚合
 *      a. group
 *      b. 多维分析 https://blog.csdn.net/javahelpyou/article/details/125205920
 *  10.5.6 窗口表值函数 TVF 聚合
 *      FORM TABLE(
 *          窗口类型(TABLE 表名, DESCRIPTOR(时间字段), INTERVAL 时间)
 *      )
 *      a. 滚动窗口
 *      SELECT
 *      window_start,
 *      window_end,
 *      id , SUM(vc)
 *      sumVC
 *      FROM TABLE(
 *      TUMBLE(TABLE ws, DESCRIPTOR(et), INTERVAL '5' SECONDS))
 *      GROUP BY window_start, window_end, id;
 *  10.5.7 Over 聚合
 *      OVER 聚合为一系列有序行的每个输入计算一个聚合值
 *
 *      select
 *          agg_func(agg_col) OVER(
 *              partition by col1
 *              order by time_col
 *              range_definition
 *          ),
 *       from
 *       order by: 必须是时间戳列
 *       partition by: 标识了聚合窗口的聚合粒度
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/10/30   15:44
 * Update History:
 * Author        Time            Content
 */
public class FlinkHelloApplication {
}
