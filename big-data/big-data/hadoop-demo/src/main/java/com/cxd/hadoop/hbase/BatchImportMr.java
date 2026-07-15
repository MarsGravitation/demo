package com.cxd.hadoop.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

/**
 * Description: https://blog.csdn.net/weixin_40612128/article/details/123279778
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/2/18   13:47
 * Update History:
 * Author        Time            Content
 */
public class BatchImportMr {

    public static class BatchImportMapper extends Mapper<LongWritable, Text, NullWritable, Put> {
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, NullWritable, Put>.Context context) throws IOException, InterruptedException {
            String[] strings = value.toString().split("\t");
            String rowKey = strings[0];
            String columnFamily = "info";
            String name = "name";
            String v = strings[1];
            Put put = new Put(rowKey.getBytes());
            put.addColumn(columnFamily.getBytes(), name.getBytes(), v.getBytes());
            context.write(NullWritable.get(), put);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        String inPath = args[0];
        String outTableName = args[1];
        // 设置属性对应参数
        Configuration configuration = new Configuration();
        configuration.set("hbase.table.name", outTableName);
        configuration.set("hbase.zookeeper.quorum", "nisimei1:2181");

        // 封装 job
        Job job = Job.getInstance(configuration, "Batc Import Hbase Table: " + outTableName);
        job.setJarByClass(BatchImportMr.class);

        // 指定输入路径
        FileInputFormat.setInputPaths(job, new Path(inPath));

        // 指定 map 相关的代码
        job.setMapperClass(BatchImportMapper.class);
        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(Put.class);

        TableMapReduceUtil.initTableReducerJob(outTableName,null, job);
        TableMapReduceUtil.addDependencyJars(job);

        // 禁用 reduce
        job.setNumReduceTasks(0);

        job.waitForCompletion(true);
    }
}
