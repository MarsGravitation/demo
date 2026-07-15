package com.cxd.hadoop.writable;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/2/6   14:07
 * Update History:
 * Author        Time            Content
 */
public class FlowReducer extends Reducer<Text, FlowBean, Text, FlowBean> {
    private FlowBean outV = new FlowBean();

    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Reducer<Text, FlowBean, Text, FlowBean>.Context context) throws IOException, InterruptedException {
        long totalUp = 0;
        long totalDown = 0;

        // 1. 遍历 values，将其中的上行流量，下行流量分别累加
        for (FlowBean flowBean : values) {
            totalUp += flowBean.getUpFlow();
            totalDown += flowBean.getDownFlow();
        }

        // 2. 分装 outKV
        outV.setUpFlow(totalUp);
        outV.setDownFlow(totalDown);
        outV.setSumFlow();

        // 3. 写出 outK outV
        context.write(key, outV);
    }
}
