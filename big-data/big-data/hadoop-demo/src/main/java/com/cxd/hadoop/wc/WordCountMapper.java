package com.cxd.hadoop.wc;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/2/6   10:14
 * Update History:
 * Author        Time            Content
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    Text k = new Text();
    IntWritable v = new IntWritable(1);

    /**
     * TextInputFormat 是默认的 FileInputFormat 实现类。按行读取没调记录。键是存储该行
     * 在整个文件中的起始字节偏移量。LongWritable 类型。值是这样的内容，不包括任何终止符，
     * Text 类型
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2024/2/6 14:26
     *
     * @param: key - [org.apache.hadoop.io.LongWritable]
     * @param: value - [org.apache.hadoop.io.Text]
     * @param: context - [org.apache.hadoop.mapreduce.Mapper<org.apache.hadoop.io.LongWritable]
     * @return: void
     */
    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
        // 1. 获取一行
        String line = value.toString();
        // 2. 切割
        String[] words = line.split(" ");
        // 3. 输出
        for (String word : words) {
            k.set(word);
            context.write(k, v);
        }
    }
}
