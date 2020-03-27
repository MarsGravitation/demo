package com.microwu.cxd.nio.line;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Description:     按行读取文件
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/5/20   16:33
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class NIOTest {
    public static void main(String[] args) throws IOException {

    }

    /**
     * 基本上,所有的IO都从一个Channel开始.Channel有点像流,数据可以从Channel读到Buffer,也可以从
     * Buffer写到Channel中
     *
     * Selector允许单个线程处理多个Channel.
     * 要使用Selector,得向Selector注册Channel,然后调用它的select方法,这个方法会一直阻塞到某个注册的通道有事件就绪
     * 一旦这个方法返回,线程就可以处理这些事件.
     *
     * Channel:
     *  既可以从通道中读取数据,也可以写数据到通道中
     *  通道可以异步的读写
     *  通道中的数据总是先要读到一个Buffer,或者从一个Buffer中写入
     *
     * Buffer:
     *  缓冲区的本质上是一块可以写入数据,然后可以从中读取数据的内存.这块内存被包装成NIO Buffer对象.
     *  步骤:
     *      1.写入数据到Buffer
     *      2.调用flip()方法
     *      3.从Buffer中读取数据
     *      4.调用clear()方法或者compact()方法
     *
     *  当向buffer写入数据时,Buffer会记录下写了多少数据.一旦要读取数据,需要通过flip()方法将buffer从写模式切换到读模式.
     *  在读模式下,可以读取之前写入到buffer的所有数据
     *
     *  一旦读完了所有的数据,就需要清空缓冲区,让它可以再次被写入.有两种方式清空缓冲区:调用clear()或者compact()方法.
     *  clear()方法会清空整个缓冲区.compact()方法只会清除已经读过的数据.任何为读过的数据都被移到缓冲区的起始处,
     *  新写入的数据将放到缓冲区未读数据的后面.
     *
     *  Buffer的capacity,position和limit
     *  缓冲区的本质上是一块可以写入数据,然后可以个从中读取数据的内存.
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/5/20  17:28
     *
     * @param   	
     * @return  void
     */
    public void test01() throws IOException {
        RandomAccessFile file = new RandomAccessFile("E:\\Project\\dp-server\\data\\a.txt", "r");
        FileChannel channel = file.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(10);
        int read = channel.read(buffer);
        // -1代表文件的末尾
        while(read != -1) {
            System.out.println("Read: " + read);
            buffer.flip();
            while(buffer.hasRemaining()) {
                System.out.println((char)buffer.get());
            }
            buffer.clear();
            read = channel.read(buffer);
        }
        file.close();
    }

    public void test02() throws IOException {
        RandomAccessFile file = new RandomAccessFile("", "r");
        // 获取通道
        FileChannel channel = file.getChannel();
        // 设置缓冲区大小
        ByteBuffer buffer = ByteBuffer.allocate(20);
        // 读文件
        while((channel.read(buffer) != -1)) {
            // 切换状态
            buffer.flip();
            while(buffer.hasRemaining()) {
                System.out.println((char)buffer.get());
            }
            buffer.clear();
        }
        file.close();
    }
}