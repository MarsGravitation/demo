package com.cxd.hadoop.writable;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Description: 自定义 bean 对象实现序列化接口 Writable
 *
 *  1. 必须实现 Writable 接口
 *  2. 反序列化时，需要反射调用空参构造函数，所以不许有空参构造
 *  3. 重写序列化方法 write
 *  4. 重写反序列化方法 readFileds
 *  5. 注意反序列化的顺序和序列化的顺序完全一致
 *  6. 要想把结果显示在文件中，需要重写 toString
 *  7. 如果需要将自定义的 bean 放在 key 中传输，则还需要实现 Comparable 接口，
 *  因为 MapReduce 框架中的 Shuffle 过程要求对 key 必须能排序。
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/2/6   13:52
 * Update History:
 * Author        Time            Content
 */
public class FlowBean implements Writable {

    /**
     * 上行流量
     */
    private long upFlow;

    /**
     * 下行流量
     */
    private long downFlow;

    /**
     * 总流量
     */
    private long sumFlow;

    // 无参构造
    public FlowBean() {

    }

    // 提供 getter 和 setter 方法
    public long getUpFlow() {
        return upFlow;
    }

    public void setUpFlow(long upFlow) {
        this.upFlow = upFlow;
    }

    public long getDownFlow() {
        return downFlow;
    }

    public void setDownFlow(long downFlow) {
        this.downFlow = downFlow;
    }

    public long getSumFlow() {
        return sumFlow;
    }

    public void setSumFlow() {
        this.sumFlow = this.upFlow + this.downFlow;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(upFlow);
        out.writeLong(downFlow);
        out.writeLong(sumFlow);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.upFlow = in.readLong();
        this.downFlow = in.readLong();
        this.sumFlow = in.readLong();
    }

    @Override
    public String toString() {
        return upFlow + "\t" + downFlow + "\t" + sumFlow;
    }
}
