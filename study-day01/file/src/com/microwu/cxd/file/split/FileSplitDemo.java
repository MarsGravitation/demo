package com.microwu.cxd.file.split;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Description:
 * Author:   Administration
 * Date:     2019/3/30 11:28
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class FileSplitDemo {
    public static void main(String[] args) throws IOException {
        splitFile("G:\\workspace\\task\\王钻\\数据库\\WeChat Files\\wxid_rs6uf3wu28lm22\\FileStorage\\File\\2020-10\\王砖商城数据0928(1)\\online_and_mid.csv", 2);
        // qieGe(20, "G:\\workspace\\task\\王钻\\数据库\\4月离网用户已登录王钻商城.csv");
    }

    //文件分割的方法（方法内传入要分割的文件路径以及要分割的份数）
    public static void splitFile(String filePath, int fileCount) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        FileChannel inputChannel = fis.getChannel();
        final long fileSize = inputChannel.size();
        long average = fileSize / fileCount;//平均值
        long bufferSize = 200; //缓存块大小，自行调整
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.valueOf(bufferSize + "")); // 申请一个缓存区
        long startPosition = 0; //子文件开始位置
        long endPosition = average < bufferSize ? 0 : average - bufferSize;//子文件结束位置
        for (int i = 0; i < fileCount; i++) {
            if (i + 1 != fileCount) {
                int read = inputChannel.read(byteBuffer, endPosition);// 读取数据
                readW:
                while (read != -1) {
                    byteBuffer.flip();//切换读模式
                    byte[] array = byteBuffer.array();
                    for (int j = 0; j < array.length; j++) {
                        byte b = array[j];
                        if (b == 10 || b == 13) { //判断\n\r
                            endPosition += j;
                            break readW;
                        }
                    }
                    endPosition += bufferSize;
                    byteBuffer.clear(); //重置缓存块指针
                    read = inputChannel.read(byteBuffer, endPosition);
                }
            } else {
                endPosition = fileSize; //最后一个文件直接指向文件末尾
            }
            String lastName = filePath.substring(filePath.lastIndexOf("."));//截取后面的文件后缀
            String subName = filePath.substring(0, filePath.lastIndexOf("."));//截取文件的前缀
            FileOutputStream fos = new FileOutputStream(subName + "_part_" + (i + 1) + lastName);
            FileChannel outputChannel = fos.getChannel();
            inputChannel.transferTo(startPosition, endPosition - startPosition, outputChannel);//通道传输文件数据
            outputChannel.close();
            fos.close();
            startPosition = endPosition + 1;
            endPosition += average;
        }
        inputChannel.close();
        fis.close();

    }

    public static void qieGe(int a, String srcPath) {
        File srPath = new File(srcPath);//获得分割文件
        long size = srPath.length();//获得文件大小
        long b = size / a;//每一份分割文件大小
        String lastName = srcPath.substring(srcPath.lastIndexOf("."));//截取后面的文件后缀
        String subName = srcPath.substring(0, srcPath.lastIndexOf("."));//截取文件的前缀
        BufferedInputStream bu = null;
        BufferedOutputStream ty = null;
        try {
            bu = new BufferedInputStream(new FileInputStream(srPath));
            int res = 0;
            int sizeCount = 0;
            for (int i = 0; i < a; i++) {

                ty = new BufferedOutputStream(new FileOutputStream(new File(subName + i + lastName)));
                byte[] b1 = new byte[1024 * 8];
                while ((res = bu.read(b1)) != -1) {
                    ty.write(b1, 0, res);
                    sizeCount += res;
                    if (sizeCount >= b) {
                        System.out.println(sizeCount);
                        break;
                    }
                    ty.flush();
                }

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                bu.close();
                ty.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        System.out.println("截取完成！");
    }

}