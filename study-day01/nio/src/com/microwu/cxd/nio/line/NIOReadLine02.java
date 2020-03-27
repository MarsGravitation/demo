package com.microwu.cxd.nio.line;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Description:    按行读取文件
 * 思路:
 * 将内容读入字节数组,判断是否为换行,如果有换行,将内容进行拼接
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/5/21   10:00
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class NIOReadLine02 {

    public static void main(String[] args) throws IOException {
        new NIOReadLine02().test();
    }

    public void test() throws IOException {
//指定读取文件所在位置
        File file = new File("E:\\Project\\dp-server\\data\\a.txt");
        FileChannel fileChannel = new RandomAccessFile(file, "r").getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        //使用temp字节数组用于存储不完整的行的内容
        byte[] temp = new byte[0];
        while (fileChannel.read(byteBuffer) != -1) {
            byte[] bs = new byte[byteBuffer.position()];
            byteBuffer.flip();
            byteBuffer.get(bs);
            byteBuffer.clear();
            int startNum = 0;
            //判断是否出现了换行符，注意这要区分LF-\n,CR-\r,CRLF-\r\n,这里判断\n
            boolean isNewLine = false;
            for (int i = 0; i < bs.length; i++) {
                if (bs[i] == 10) {
                    isNewLine = true;
                    startNum = i;
                }
            }

            if (isNewLine) {
                //如果出现了换行符，将temp中的内容与换行符之前的内容拼接
                byte[] toTemp = new byte[temp.length + startNum];
                System.arraycopy(temp, 0, toTemp, 0, temp.length);
                System.arraycopy(bs, 0, toTemp, temp.length, startNum);
                System.out.println(new String(toTemp));
                //将换行符之后的内容(去除换行符)存到temp中
                temp = new byte[bs.length - startNum - 1];
                System.arraycopy(bs, startNum + 1, temp, 0, bs.length - startNum - 1);
                //使用return即为单行读取，不打开即为全部读取
//                return;
            } else {
                //如果没出现换行符，则将内容保存到temp中
                byte[] toTemp = new byte[temp.length + bs.length];
                System.arraycopy(temp, 0, toTemp, 0, temp.length);
                System.arraycopy(bs, 0, toTemp, temp.length, bs.length);
                temp = toTemp;
            }

        }
        if (temp.length > 0) {
            System.out.println(new String(temp));
        }

    }
}