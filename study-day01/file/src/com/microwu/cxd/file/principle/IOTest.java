package com.microwu.cxd.file.principle;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Description:
 *  程序运行在内存以及 IO 的体现：
 *      1. 在整个内存空间中，跑着各种各样的程序，有 Java 程序，C 程序，它们共用一块内存空间
 *      2. 对于 Java 程序，JVM 会申请一块堆空间，通过 Xmx 可以设置，其余空间是堆外空间，其中每个线程
 *          有自己的线程栈，保证线程内存隔离，堆空间使用完以后，会触发 Full GC，堆外空间所有进程可共享
 *          使用，无限制
 *      3. 所有系统运行的程序都必须通过操作系统内核进行 IO 操作，操作系统也是程序，也需要一定的内存空间
 *
 *
 *
 * https://www.cnblogs.com/jing99/p/11945245.html
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/28   15:29
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class IOTest {

    /**
     * 写数据时不管是 C 还是 Java 都会有两个缓冲区，一个是操作系统的缓冲区 sys buffer，还有一个是
     * 程序的缓冲区 program buffer。那么刚刚的 flush 操作是把程序的缓冲区内容写到了系统缓冲区，还是
     * 把系统缓冲区的内容刷到了硬盘呢？
     *
     * 结论：实际上 FileWriter 基本 IO 是没有写程序缓存的，那么实际上 FileWriter 的每次 write 操作都发生了
     *      系统调用，直接写到了内核的系统缓冲区，然后当调用 flush 操作时，系统缓冲区的内容在刷新到硬盘上
     *
     *      无论是 InputStream 还是 FileWriter，都是底层 IO，是直接调用内核的，因此写入都是直接写入到内核的系统
     *      buffer，因此在使用 IO 的时候不要使用这类底层 IO，否则会发生大量的系统调用，降低系统性能，而是应该
     *      先写到程序 buffer 然后再调用系统 IO，当程序 buffer 满了后才通过系统调用写到系统 buffer 空间中，这样
     *      减少了大量系统调用，提升了性能
     *
     * 什么时候系统 buffer 中的数据才写入硬盘呢？
     *  1. 系统 buffer 满了
     *  2. flush 操作，也就是发生了 fsync 系统调用
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/28  15:37
     *
     * @param
     * @return  void
     */
    public static void fileIO() throws IOException, InterruptedException {
        File file = new File("E:\\Project\\program-data\\file\\hello.txt");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write("hello");
        fileWriter.write("world");
        fileWriter.write("\nhello world!");

        Thread.sleep(100000);

        fileWriter.flush();
        fileWriter.close();
    }

    /**
     * 利用程序的缓冲区
     * 注意：这里 FileOutputStream 可以控制是追加还是覆盖
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/28  16:13
     *
     * @param
     * @return  void
     */
    public static void bufferedIO() throws IOException {
        File file = new File("E:\\Project\\program-data\\file\\hello.txt");
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file), 1024);
        bufferedOutputStream.write("hello\nworld\n".getBytes());
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }

    /**
     * 直接写入内存，类似于 redis，是对内存进行直接操作，也能提高不少效率
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/28  16:16
     *
     * @param   	
     * @return  void
     */
    public static void memoryIO() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        // 字节数组输出流在内存中创建一个字节数组缓冲区，所有发送到输出流的数据保存在该字节数组缓冲中
        byteArrayOutputStream.write("hello world".getBytes());
    }

    /**
     * 1. 如果数据在堆内，那么在写入磁盘时，会先序列化后拷贝到堆外，然后堆外再 write 到系统内核缓冲区
     *  内核缓冲区通过系统调用 fsync 写入到磁盘
     * 2. 如果数据是在堆外内存，那么也需要先拷贝到内核缓冲区，在 fsync 系统调用后才写入到磁盘
     * 3. 通过系统调用 mmap 申请一块虚拟的地址空间，这片空间用户程序和系统内核都可以访问到
     *
     * 申请 mapp 直接映射空间，数据无需由用户空间拷贝到系统空间，节省了一次拷贝的时间损耗，提升了性能
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/28  16:24
     *
     * @param
     * @return  void
     */
    public static void randomIO() throws IOException {
        File file = new File("E:\\Project\\program-data\\file\\hello.txt");
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
//        randomAccessFile.write("hello world\n hello ChengXuDong".getBytes());
        FileChannel channel = randomAccessFile.getChannel();

        /**
         * 堆外的数据如果想写到磁盘上，通过系统调用，经历数据从用户空间拷贝到内核空间
         * 堆外 mapedBuffer 的数据内核直接处理
         */

        // 分配在堆上 heap 空间
//        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 分配在了堆外 off heap 空间
//        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        // mapp 内核系统调用 堆外空间，直接映射
        MappedByteBuffer byteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 2018);

        byteBuffer.put("byteBuffer testing".getBytes());

//        randomAccessFile.seek(12);
//        randomAccessFile.write("******".getBytes());
    }

    public static void main(String[] args) throws IOException, InterruptedException {
//        fileIO();
//        bufferedIO();
        randomIO();
    }

}