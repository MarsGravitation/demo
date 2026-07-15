package com.cxd.flink.v2;

/**
 * Description:
 *
 * 10.2.4 将动态表转换为流
 *  a. 仅追加流
 *  b. 撤回流
 *      添加消息，撤回消息，更新操作 = 撤回 + 添加
 *  c. 插入更新流
 *      更新插入和删除消息
 *      upsert 是 update 和 insert
 *      upsert by key / delete by key
 *
 * 10.3 时间属性
 *  时间属性的数据类型必须是 TIMESTAMP
 *  10.3.1 事件时间
 *      创建表 DDL 中定义
 *      ts TIMESTAMP(3),
 *      WATERMARK FOR ts AS ts - INTERVAL '5' SECOND
 *  10.3.2 处理事件
 *      必须要额外声明一个字段，专门用来保存当前的处理时间
 *      ts AS PROCTIME()
 *
 *  10.4 DDL 数据定义
 *  10.4.2 表
 *      a. 物理列
 *      b. 元数据列
 *          record_time TIMESTAMP_LTZ(3) METADATA FROM 'timestamp'
 *      如果自定义列名称和 metadata 字段名称一样，FROM xxx 子句可以省略
 *      c. 计算列
 *      d. 定义 Watermark
 *          |- WATERMARK FOR col AS col
 *          时间戳只会越来越大，不存在相等的情况，相等和小于被视为迟到的数据
 *          |- WATERMARK FOR col AS col - INTERVAL '0.0001' SECOND
 *          允许有相同的时间戳
 *          |- WATERMARK FOR col AS col - INTERVAL 'string' timeUnit
 *          有界无序，乱序，常用
 *      e. primary key
 *      f. partitioned by
 *      g. with
 *          指定外部存储系统的元数据信息
 *      h. like
 *          基于现有表的定义创建表
 *      i. AS select_statement
 *          通过查询的结果创建和填充表
 *  10.5.1 with
 *      公共表表达式
 *
 *  10.5.6 窗口表值函数聚合
 *      FROM TABLE (
 *          窗口类型(TABLE 表名, DESCRIPTOR(时间字段), INTERVAL 时间)
 *      )
 *      GROUP BY [window_start,] [window_end]
 *
 *  10.5.7 Over 聚合
 *      OVER 聚合为一系列有序行的每个输入行计算一个聚合值
 *
 *  10.6.1 Kafka
 *      更新操作，普通 kafka 连接器无法满足，需要使用 Upsert Kafka 连接器
 *
 *  10.9 FlinkSQL
 *  10.9.3 创建表
 *      a. 连接器表
 *      TableResult = executeSql(DDL)
 *      b. 虚拟表
 *      TABLE = sqlQuery(SQL)
 *      table 对象并没有在表环境中注册，如果希望直接在 SQL 中使用，需要将这个中间结果表注册到环境中
 *
 *      tableEnv.createTemporaryView("table", table);
 *
 *      创建了一个 虚拟表，与视图非常类似
 *
 *  10.9.4 表的查询
 *      a. SQL
 *      sqlQuery - 执行的是查询操作
 *      executeSql - DDL/INSERT
 *
 *      b. TABLE API
 *      from - 从表环境中得到一个 TABLE 对象
 *
 *  10.9.6 表和流的转换
 *
 *
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/11/26   10:12
 * Update History:
 * Author        Time            Content
 */
public class FlinkSqlDemo {
}
