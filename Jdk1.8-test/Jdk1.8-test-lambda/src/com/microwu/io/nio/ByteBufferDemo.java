package com.microwu.io.nio;

/**
 * Description:
 *
 * https://www.jianshu.com/p/7731e7e5c59d
 *
 * 写缓冲区过程：刚开始写数据时，limit = capacity，如果缓冲区中之前没有数据
 * position = 0，否则不为 0
 *
 * 读缓冲区过程：从缓冲区中读数据，position = 0，使用 limit 区分读取的结尾。
 * 所以有数据位置段是 0 ~ limit，没有数据位置段是 limit ~ capacity
 *
 * 写读缓冲区转换过程：写完数据之后，position != 0,limit = capacity
 * 此时开始读取缓冲区，flip(),limit = position,position = 0
 *
 * 读写缓冲区转换过程：读完数据之后，缓冲区状态：position = limit。
 * 此时需要重新开始写，position = 0, limit = capacity。这是 clear()
 *
 * 总结：
 *  1. 在写完缓冲区去读的时候，使用 flip 方法，limit = position，position = 0
 *  2. 读完缓冲区去写的时候，使用 clear 方法，position = 0，limit = capacity
 *
 *
 * https://mp.weixin.qq.com/s?__biz=MzU1NjY0NzI3OQ==&mid=2247486375&idx=3&sn=8fe7e1d9b6c0c532bb1c3b669a06a5bf&chksm=fbc09da7ccb714b172f52f12189968edc7b6fbd1bc2980543c1212a020e93e06a59106758cda&scene=21#wechat_redirect
 *
 * 堆外内存，也叫直接内存
 * 好处之一：加快 I/O 速度，JVM -> 堆外空间 -> 内核空间
 *
 * 本质上就是 java 虚拟机在外面调用 malloc 分配了一块连续的内存区域
 *
 * 堆内保存了 堆外地址 -> 堆外内存
 *
 * 释放内存时机:
 * DirectByteBuffer -> Cleaner -> free()
 *
 * 默认情况时只有 GC 发生开始清理堆外内存的引用的 java 对象时，才开始顺带
 * 清理堆外内存
 *
 * 建议：System.gc() 回收直接内存
 *
 * // 加上这几行代码,手动unmap
 *             Method m = FileChannelImpl.class.getDeclaredMethod("unmap",
 *                     MappedByteBuffer.class);
 *             m.setAccessible(true);
 *             m.invoke(FileChannelImpl.class, buffer);
 *
 * ByteBufferSupport.unmap(mappedByteBuffer);
 * mappedByteBuffer = null;
 *
 * if(mmap instanceof DirectBuffer){
 *      ((DirectBuffer) mmap).cleaner().clean();
 *  }
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/8/30   14:57
 * Update History:
 * Author        Time            Content
 */
public class ByteBufferDemo {

    public static void test() {

    }

}
