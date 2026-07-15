package com.microwu.io.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Description:
 *
 * https://www.jianshu.com/p/948f60a8e64e
 *
 * 1. 初识 IO
 *  特点：采用面向流的操作。IO 流说是没有缓存的概念，所以就需要每次从流中一个一个字节的去
 *  读或者每次从流中一个一个字节的去写。而且我们知道流本身是单工的数据通信。IO 可以分为
 *  文件 IO 和网络 IO。文件 IO 中的 read 和 write 都是阻塞的，网络 IO 中的 read、write、accept 等
 *  都是阻塞的。由于 IO 流是阻塞的又叫做 BIO。
 *
 * 2. 初识 NIO
 *  采用面向缓存区的操作。NIO 采用缓冲区的概念，先将数据都缓存再缓冲区，缓冲区采用开
 *  始读写的位置 position、读写结束位置 limit、缓冲区最大容量 capacity 等标记来设计。
 *  NIO 还采用了通道的概念，可以进行全双工数据通信。同时 NIO 分为文件 NIO 和网络 NIO。
 *
 *  a. 文件 NIO
 *      文件 NIO 中的通道是一种阻塞式。
 *  b. 网络 NIO
 *
 * 区别
 *  IO 流的特点就是建立连接，然后去读写就行了。对于是否可读可写通过阻塞等方式来处理。
 *  NIO 的特定就是将读写拆分为准备读写和真正读写两个方面，在准备读写阶段采用非阻塞的方
 *  式来处理，从而真正读写的时候实际上还是阻塞的方式。
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/8/30   14:11
 * Update History:
 * Author        Time            Content
 */
public class NioDemo {

    public static void main(String[] args) throws IOException {
        testSize();
    }

    public static void testSize() throws IOException {
        File file = new File("D:\\data\\test\\cadIn\\C_B_10_BSDM_CHL_20220819_0001_A_1.XML.gz");
        FileInputStream inputStream = new FileInputStream(file);
        FileChannel channel = inputStream.getChannel();
        System.out.println(file.length());
        System.out.println(channel.size());
    }

    public static void writeByBIO(String path, String context) {
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(path, true);

            for (int i = 0; i < 3; i++) {
                fos.write((context + '\n').getBytes());
            }
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

    public static void readByBIO(String path) {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(path);
            int length;

            while ((length = fis.read()) != -1) {
                System.out.println((char) length);
            }
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

    /**
     * 第一个线程写入时，第二个线程和第三个线程虽然已经已经启动，但是处于
     * 线程阻塞挂起的状态。
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/8/30 14:19
     *
     * @param:
     * @return: void
     */
    public static void test() {
        new Thread(() -> {
            writeByBIO("", "1");
        }).start();
        new Thread(() -> {
            writeByBIO("", "2");
        }).start();
        new Thread(() -> {
            writeByBIO("", "3");
        }).start();
    }

    public static void writeByNIO(String path, String context) {
        FileOutputStream fos = null;
        FileChannel fc = null;

        try {
            fos = new FileOutputStream(path, true);
            fc = fos.getChannel();
            ByteBuffer bb = ByteBuffer.allocate(1024 * 1024 * 5);

            for (int i = 0; i < 3; i++) {
                bb.put((context + '\n').getBytes());
            }

            bb.flip();
            fc.write(bb);
            bb.clear();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fc != null) {
                try {
                    fc.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void readByNIO(String path) {
        FileInputStream fis = null;
        FileChannel fc = null;

        try {
            fis = new FileInputStream(path);
            fc = fis.getChannel();
            ByteBuffer bb = ByteBuffer.allocate(1024 * 1024 * 5);
            int length;

            while ((length = fc.read(bb)) != -1) {
                bb.flip();
                System.out.println(new String(bb.array(), 0, length));
                bb.clear();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (fc != null) {
                try {
                    fc.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
