package com.microwu.cxd.file.practice;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * 顺序读写
 *
 * https://blog.csdn.net/jy317358306/article/details/109365865
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/5/14  17:52
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class SequentialRAW {

    /**
     * 顺序写
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/5/14     17:56
     *
     * @param filePath
     * @param content
     * @param index
     * @return long
     */
    public long fileWrite(String filePath, String content, int index) {
        File file = new File(filePath);
        RandomAccessFile randomAccessTargetFile;
        // 操作系统提供的一个内存映射的机制的类
        MappedByteBuffer map;
        try {
            randomAccessTargetFile = new RandomAccessFile(file, "rw");
            FileChannel targetFileChannel = randomAccessTargetFile.getChannel();
            map = targetFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, (long) 1024 * 1024 * 1024);
            map.position(index);
            map.put(content.getBytes(StandardCharsets.UTF_8));
            return map.position();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return 0L;
    }

    /**
     * 顺序读
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/5/14     17:56
     *
     * @param filePath
     * @param index
     * @return java.lang.String
     */
    public String fileRead(String filePath, long index) {
        File file = new File(filePath);
        RandomAccessFile randomAccessTargetFile;
        // 操作系统提供的一个内存映射机制的类
        MappedByteBuffer map;
        try {
            randomAccessTargetFile = new RandomAccessFile(file, "rw");
            FileChannel targetFileChannel = randomAccessTargetFile.getChannel();
            map = targetFileChannel.map(FileChannel.MapMode.READ_WRITE, 0, index);
            byte[] bytes = new byte[1024 * 10];
            map.get(bytes, 0, (int) index);
            return new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return "";
    }

}
