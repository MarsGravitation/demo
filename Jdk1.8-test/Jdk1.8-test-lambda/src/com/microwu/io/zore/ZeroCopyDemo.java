package com.microwu.io.zore;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Description:
 *
 * https://blog.csdn.net/qq_32655207/article/details/116563236
 *
 * 一、nio
 *  1. NIO 概念
 *  io 面向流，nio 面向缓冲区
 * 二、pageCache
 * 三、mmap 和 sendfile
 *  3.2 mmap
 *      可以将一段用户空间内存映射到内核空间，不需要在用户态和内核态之间拷贝
 *  3.3 sendfile
 *      在java中可以通过transferFrom和transferTo实现
 * 四、pageCache 预读和顺序读写
 *
 *
 * 性能：https://www.jianshu.com/p/eece39beee20
 * 场景：对2G的文件进行顺序写入(go语言编写)
 *
 * 每次写入大小 | mmap 耗时 | write 耗时
 * --------------- | ------- | -------- | --------
 * | 1 byte | 22.14s | >300s
 * | 100 bytes | 2.84s | 22.86s
 * | 512 bytes | 2.51s | 5.43s
 * | 1024 bytes | 2.48s | 3.48s
 * | 2048 bytes | 2.47s | 2.34s
 * | 4096 bytes | 2.48s | 1.74s
 * | 8192 bytes | 2.45s | 1.67s
 * | 10240 bytes | 2.49s | 1.65s
 *
 * 每次读取大小 | mmap 耗时 | write 耗时
 * --------------- | ------- | -------- | --------
 * | 1 byte | 8215.4ms | > 300s
 * | 100 bytes | 86.4ms | 8100.9ms
 * | 512 bytes | 16.14ms | 1851.45ms
 * | 1024 bytes | 8.11ms | 992.71ms
 * | 2048 bytes | 4.09ms | 636.85ms
 * | 4096 bytes | 2.07ms | 558.10ms
 * | 8192 bytes | 1.06ms | 444.83ms
 * | 10240 bytes | 867.88µs | 475.28ms
 *
 * 结论：
 * 可以看到mmap在100byte写入时已经基本达到最大写入性能，而write调用需要在4096(也就是一个page size)时，才能达到最大写入性能。
 *
 * 从测试结果可以看出，在写小数据时，mmap会比write调用快，但在写大数据时，反而没那么快(但不太确认是否go的slice copy的性能问题，没时间去测C了)。
 *
 * 由上可以看出，在read上面，mmap比write的性能差别还是很大的。测试结果与理论推导吻合。
 *
 * https://mp.weixin.qq.com/s?__biz=MzU1NjY0NzI3OQ==&mid=2247486550&idx=2&sn=cfc84a86af50e5b27bf9804f17971bb3&chksm=fbc09a56ccb7134025110d6458900885cc4c95f8bcd1da086e45cd03cff49f54958e04247f14&scene=178&cur_album_id=1338702322071076864#rd
 *
 *
 *
 * 其他：
 *  https://blog.csdn.net/agonie201218/article/details/123791047
 *  https://andyoung.blog.csdn.net/article/details/123790519
 *
 *  https://zhuanlan.zhihu.com/p/559187654
 *  https://zhuanlan.zhihu.com/p/490947029
 *
 *  https://www.cnblogs.com/f-ck-need-u/p/7615914.html
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/8/31   9:57
 * Update History:
 * Author        Time            Content
 */
public class ZeroCopyDemo {

    public void testChannel() throws IOException {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;

        try {
            inputStream = new FileInputStream("");
            outputStream = new FileOutputStream("");

            inputChannel = inputStream.getChannel();
            outputChannel = outputStream.getChannel();

            // 分配缓冲区
            ByteBuffer bb = ByteBuffer.allocate(1024);

            // 将通道数据存入缓冲区
            while (inputChannel.read(bb) != -1) {
                // 切换成读模式
                bb.flip();
                // 缓冲区中的数据写到通道中
                outputChannel.write(bb);
                // 清空缓冲区
                bb.clear();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (inputChannel != null) {
                inputChannel.close();
            }
            if (outputChannel != null) {
                outputChannel.close();
            }
        }
    }

    static final int BUFFER_SIZE = 1024;

    /**
     * MappedByteBuffer - java 文件内存映射方案，读写性能极高
     * 如果文件比较大的话可以考虑分段映射
     *
     * 由于 MappedByteBuffer 申请的是直接内存，因此不收 Minor GC 控制，
     * 只能在发生 Full GC 时才能被回收，因此 java 提供了 DirectByteBuffer。
     * 它是 MappedByteBuffer 的子类，维护一个 Cleaner 对象来完成内存回收。
     * 因此它既可以通过 Full GC，也可以调用 clean() 来回收
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/8/31 10:08
     *
     * @param: file - [java.io.File]
     * @return: void
     */
    public static void fileReadWithMmap(File file) {
        byte[] b = new byte[BUFFER_SIZE];
        int len = (int) file.length();
        MappedByteBuffer buff;
        try (FileChannel channel = new FileInputStream(file).getChannel()) {
            // 将文件所有字节映射到内存中，返回 MappedByteBuffer
            buff = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            for (int offset = 0; offset < len; offset += BUFFER_SIZE) {
                if (len - offset > BUFFER_SIZE) {
                    buff.get(b);
                } else {
                    buff.get(new byte[len - offset]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fileCopyWithFileChannel(File fromFile, File toFile) {
        try (
                FileChannel fileChannelInput = new FileInputStream(fromFile).getChannel();
                FileChannel fileChannelOutput = new FileOutputStream(toFile).getChannel()
                ) {
            fileChannelInput.transferTo(0, fileChannelInput.size(), fileChannelOutput);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
