package com.cxd.hadoop.mapjoin;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/2/6   16:14
 * Update History:
 * Author        Time            Content
 */
public class MapJoinDriver {

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException, ClassNotFoundException {
        // 1. 获取 job 信息
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);

        // 2. 设置加载 jar 包路径
        job.setJarByClass(MapJoinDriver.class);

        // 3. 设置 mapper
        job.setMapperClass(MapJoinMapper.class);

        // 4. 设置 Map 输出 KV 类型
        job.setOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        // 加载缓存数据
        job.addCacheFile(new URI(args[0]));
        // map 端 join 的逻辑不需要 reduce 阶段，设置 reduceTask 数量为 0
        job.setNumReduceTasks(0);

        // 设置输入输出路径
        FileInputFormat.setInputPaths(job, new Path(args[1]));
        FileOutputFormat.setOutputPath(job, new Path(args[2]));

        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }

}
