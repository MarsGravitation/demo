package com.cxd.hadoop.mapjoin;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/2/6   16:16
 * Update History:
 * Author        Time            Content
 */
public class MapJoinMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

    private Map<String, String> pdMap= new HashMap<>();
    private Text text = new Text();

    /**
     * 任务开始前将 pd 数据缓存进 pdMap
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2024/2/6 16:17
     *
     * @param: context - [org.apache.hadoop.mapreduce.Mapper<org.apache.hadoop.io.LongWritable]
     * @return: void
     */
    @Override
    protected void setup(Mapper<LongWritable, Text, Text, NullWritable>.Context context) throws IOException, InterruptedException {
        // 通过缓存文件得到小表数据 pd.txt
        URI[] cacheFiles = context.getCacheFiles();
        Path path = new Path(cacheFiles[0]);

        // 获取文件系统对象，并开流
        FileSystem fs = FileSystem.get(context.getConfiguration());
        FSDataInputStream fis = fs.open(path);

        // 通过包装流转换为 reader，方便按行读取
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

        // 逐行读取，按行处理
        String line;
        while ((line = reader.readLine()) != null) {
            String[] split = line.split("\t");
            pdMap.put(split[0], split[1]);
        }
    }

    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, NullWritable>.Context context) throws IOException, InterruptedException {
        // 读取大表数据
        String[] fields = value.toString().split("\t");

        // 通过大表每行数据的 pid 去 pdMap 里面取出 pname
        String pname = pdMap.get(fields[1]);
        // 将大表每行数据的 pid 替换为 pname
        text.set(fields[0] + "\t" + pname + "\t" + fields[2]);
        // 写出
        context.write(text, NullWritable.get());
    }
}
