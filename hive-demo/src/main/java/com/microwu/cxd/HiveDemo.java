package com.microwu.cxd;

/**
 * Description:
 *
 * 1.1 hive
 *  hive 本质是将 hive sql 转换成 mapreduce 程序
 *      1) hive 中每张表的数据存储在 hdfs
 *      2) hive 分析数据底层的实现是 MapReduce
 *      3) 执行程序运行在 yarn 上
 *  metasotre 记录对应表的路径
 *
 * 3.1.1 创建数据库
 *  1) 不指定路径，默认路径为 ${hive.metastore.warehouse.dir}/database_name.db
 *  2) 指定路径，create database db_hive location '/db_hive2';
 *  3) 创建一个数据库，带有 dbproperties
 *
 * 3.2 表
 *  3.2.1 创建表
 *      create [temporary] [external] table [if not exists]
 *      db_name.table_name
 *      [col_name data_type]
 *      comment table_comment
 *      [partitioned by (col_name data_type)]
 *      [clustered by (col_name...)]
 *      [sorted by (col_name) [ASC|DESC]] into num buckets buckets
 *      row format row_format
 *      stored as file_format
 *      location hdfs_path
 *
 *      temporary: 临时表，只在当前会话可见，会话结束，表会被删除
 *      external: 外部表，对应的是内部表。管理表意味着 hive 会完全接管该表，包括
 *      元数据和 hdfs 中的数据。而外部表意味着 hive 只接管元数据，而不完全接管 hdfs
 *      的数据
 *      data_type:
 *      partitioned by: 分区表
 *      clustered by ... sorted by ... into ... buckets: 分桶表
 *      row format: SERDE 是序列化和非序列化的简写。
 *      stored as: 指定文件格式
 *      location: 表对应的 hdfs 路径
 *      tabproperties: 配置表的一些 kv 键值对参数
 *
 *  3.2.1.2
 *      内部表删除后，数据文件也被删除；外部表删除后，数据文件还在
 *
 *      create table as select;
 *      create table like;
 *
 *  4.1 load
 *      将文件导入 hive 表中
 *      load data [local] inpath 'filepath' [overwrite] into table tablename [partition (p=v)];
 *  4.2 insert
 *      insert into | overwrite table tablename [partition (p=v)] select_statement;
 *
 *      count(*) -> 首先分区 group by -> count，然后 reduce group by -> count
 *
 *  8.5 窗口函数 - 开窗函数
 *      1) 聚合函数 max,min,sum,avg,count
 *      2) 跨行取值函数
 *          lead/lag: 获取当前行的上/下边某行、某个字段的值
 *          first_value/last_value: 获取窗口函数内某一列的第一个值/最后一个值
 *      3) 排名函数
 *          rank, dense_rank, row_number
 *
 *  10.1 分区表
 *      分区表就是把一张大表的数据按照业务需要分散的存储到多个目录，每个目录就称为
 *      该表的一个分区。在查询时通过 where 子句中的表达式选择查询所需要的分区，这样
 *      的查询效率会提高很多。
 *
 *  10.2 分桶表
 *      分区针对的是数据的存储路径，分桶针对的是数据文件
 *      首先为每行数据计算一个指定字段的数据的 hash 值，然后模一个分桶数，最后将取模运算结果
 *      相同的行，写入同一个文件中
 *
 *  11. 文件格式和压缩
 *      TextFile: 文本的一行内容对应 hive 表中的一行记录
 *      ORC: 列式存储
 *      Parquet: 列式存储
 *
 *  11.3 压缩
 *      TextFile
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2025/4/18   10:29
 * Update History:
 * Author        Time            Content
 */
public class HiveDemo {
}
