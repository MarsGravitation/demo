package com.microwu.io.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Description:
 *
 * https://www.jianshu.com/p/93c91fda2a9c
 * https://www.jianshu.com/p/ecfacec90357
 *
 * 介绍
 *  文件通道 FileChannel 用于读取，写入，文件的通道。FileChannel 只能被 InputStream、
 *  OutputStream、RandomAccessFile 创建。FileChannel 通过字节流创建，那么操作的缓冲
 *  就是字节缓冲区。
 *
 * 总结：
 *  1. 文件通道不能直接创建，只能通过 InputStream、OutputStream 或者 RAndomAccessFile 等
 *  创建对应的文件通道。
 *  2. 文件通道 FileChannel 从缓冲区中读取数据，使用 read 方法
 *  3. 文件通道 FileChannel 的 read 方法只能读 ByteBuffer 缓冲区
 *  4. 从缓冲区向文件通道写数据，需要使用 write 方法
 *  5. force 方法将文件通道中的数据强制写到磁盘上
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/8/30   14:33
 * Update History:
 * Author        Time            Content
 */
public class FileChannelDemo {

    public void readData(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            // 创建缓冲区
            ByteBuffer buf = ByteBuffer.allocate(1024);
            // 从通道读取数据到缓冲区
            fc.read(buf);
            // 反转缓冲区(limit 设置为 position，position 设置为 0，mark 设置为 -1)
            buf.flip();
            // 判断 position 和 limit 之间是否有元素
            while (buf.hasRemaining()) {
                // 按照字节的格式获取数据
                System.out.println((char) buf.get());
            }
            // 读完将缓冲区还原（position 设置为 0，limit 设置为 capacity，mark 设置为 -1）
            buf.clear();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void writeData(File file, String data) {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            FileChannel fc = fos.getChannel();
            // 创建缓冲区
            ByteBuffer buf = ByteBuffer.allocate(1024);
            // 将数据装入缓冲区
            buf.put(data.getBytes());
            // 反转缓冲区（limit 设置为 position，position 设置为 0，mark 设置为 -1）
            buf.flip();
            // 将缓冲区中的数据写入通道
            fc.write(buf);
            // 写完将缓冲区还原（position 设置为 0，limit 设置为 capacity，mark 设置为 -1）
            buf.clear();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
