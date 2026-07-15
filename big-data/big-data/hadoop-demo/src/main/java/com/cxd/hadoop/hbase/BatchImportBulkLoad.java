package com.cxd.hadoop.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat2;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Description:
 *
 * 利用 BulkLoad
 * 在 map 阶段，把数据封装成 put 操作，将数据生成 HBase 的底层存储文件 HFile
 * 再讲生成的 HFile 文件加载到表中
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/2/18   14:20
 * Update History:
 * Author        Time            Content
 */
public class BatchImportBulkLoad {

    public static class BulkLoadMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, ImmutableBytesWritable, Put>.Context context) throws IOException, InterruptedException {
            String[] strings = value.toString().split("\t");
            String rowKey = strings[0];
            String columnFamily = "info";
            String name = "name";
            String v = strings[1];
            ImmutableBytesWritable rowKeyWritable = new ImmutableBytesWritable(rowKey.getBytes());
            Put put = new Put(rowKey.getBytes());
            put.addColumn(columnFamily.getBytes(), name.getBytes(), v.getBytes());
            context.write(rowKeyWritable, put);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        String inPath = args[0];
        String outPath = args[1];
        String outTableName = args[2];

        // 设置属性对应参数
        Configuration configuration = new Configuration();
        configuration.set("hbase.table.name", outTableName);
        configuration.set("hbase.zookeeper.quorum", "nisimei1:2181");

        // 封装 job
        Job job = Job.getInstance(configuration, "Batc Import Hbase Table: " + outTableName);
        job.setJarByClass(BatchImportBulkLoad.class);

        // 指定输入路径
        FileInputFormat.setInputPaths(job, new Path(inPath));

        // 指定输出路径 --- 如果输出路径存在，就将其删除
        FileSystem fs = FileSystem.get(configuration);
        Path output = new Path(outPath);
        if (fs.exists(output)) {
            fs.delete(output, true);
        }
        FileOutputFormat.setOutputPath(job, output);

        // 指定 map 相关的代码
        job.setMapperClass(BulkLoadMapper.class);
        job.setMapOutputKeyClass(ImmutableBytesWritable.class);
        job.setMapOutputValueClass(Put.class);

        // 禁用 Reduce
        job.setNumReduceTasks(0);

        Connection connection = ConnectionFactory.createConnection(configuration);
        TableName tableName = TableName.valueOf(outTableName);
        HFileOutputFormat2.configureIncrementalLoad(job, connection.getTable(tableName), connection.getRegionLocator(tableName));

        job.waitForCompletion(true);
    }

}
