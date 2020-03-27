package com.microwu.cxd.nio.line;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * Description:     NIO完成按行读取
 * 基本思路:
 *  使用两个buffer,一个用作缓冲区,一个用于保存一行的数据
 *
 *  存在的问题:
 *      最后一行没有回车换行,所以最后一行读取不到
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/5/21   9:10
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class NIOReadLine01 {
    public static void main(String[] args) throws IOException {
        NIOReadLine01 nioReadLine = new NIOReadLine01();
        nioReadLine.test();
    }
    public void test() throws IOException {
        // 文件对象
        RandomAccessFile file = new RandomAccessFile("E:\\Project\\dp-server\\data\\a.txt", "r");
        // 获取通道
        FileChannel channel = file.getChannel();
        // 缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(10);
        // 行缓冲区
        ByteBuffer lineBuffer = ByteBuffer.allocate(20);
        // 读取文件至缓冲区, 返回值表示读取的字节数
        int read = channel.read(buffer);
        // 文件末尾返回 -1
        while(read != -1) {
            // System.out.println("Read: " + read);
            // 切换读模式
            buffer.flip();
            // 缓冲区还有元素时
            while(buffer.hasRemaining()) {
                // 读取一个字节
                byte b = buffer.get();
                // 回车换行符,表示一行内容
                if(b == 10 || b == 13) {
                    // 读一行内容,切换模式
                    lineBuffer.flip();
                    System.out.println("==========: " + Charset.forName("utf-8").decode(lineBuffer).toString());
                    // 清空行缓冲区
                    lineBuffer.clear();
                }else {
                    // 没有回车,向行缓冲区写
                    if(lineBuffer.hasRemaining()) {
                        lineBuffer.put(b);
                    }else {
                        // 扩容

                    }
                }
            }
            // 清空缓存区
            buffer.clear();
            // 继续读
            read = channel.read(buffer);
        }
    }
}