package com.microwu.cxd.nio.line;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/5/20   17:55
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class NIODemo {
    /**
     * 思路:
     *      两个byte数组,一个用作缓冲区,一个用来存放一行数据
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/5/20  19:14
     *
     * @param
     * @return  void
     */
    private static void readLine() throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("E:\\Project\\dp-server\\data\\a.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(10);
        int bytesRead = channel.read(buffer);
        ByteBuffer stringBuffer = ByteBuffer.allocate(20);
        while (bytesRead != -1) {
            System.out.println("读取字节数：" + bytesRead);
            //之前是写buffer，现在要读buffer
            buffer.flip();// 切换模式，写->读
            // 判断position 和 limit 之间是否还有元素
            while (buffer.hasRemaining()) {
                // 返回读取的一个字节,并将position + 1
                byte b = buffer.get();
                if (b == 10 || b == 13) { // 换行或回车
                    // flip() 从写模式切换到读模式
                    stringBuffer.flip();
                    // 这里就是一个行
                    final String line = Charset.forName("utf-8").decode(stringBuffer).toString();
                    System.out.println(line + "----------");// 解码已经读到的一行所对应的字节
                    stringBuffer.clear();
                } else {
                    if (stringBuffer.hasRemaining())
                        // put方法写入到buffer中
                        // 例如:写到一个指定的位置,或者把一个字节数组写入buffer
                        stringBuffer.put(b);
                    else { // 空间不够扩容
                        stringBuffer = reAllocate(stringBuffer);
                        stringBuffer.put(b);
                    }
                }
            }
            buffer.clear();// 清空,position位置为0，limit=capacity
            //  继续往buffer中写
            bytesRead = channel.read(buffer);
        }
        randomAccessFile.close();
    }

    private static ByteBuffer reAllocate(ByteBuffer stringBuffer) {
        final int capacity = stringBuffer.capacity();
        byte[] newBuffer = new byte[capacity * 2];
        System.arraycopy(stringBuffer.array(), 0, newBuffer, 0, capacity);
        return (ByteBuffer) ByteBuffer.wrap(newBuffer).position(capacity);
    }

    public static void main(String[] args) throws IOException {
        readLine();
    }
}