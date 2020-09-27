package com.microwu.algorithm.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/24   15:23
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BreakpointTest {

    private static final String boundary = "---------------------------7df1a33440870";
    static FileOutputStream fos;

    public static void main(String[] args) {

    }

    public static boolean parseInputStream() throws IOException {
        File file = new File("");
        fos = new FileOutputStream(new File(""), true);
        FileInputStream is = new FileInputStream(file);
        int totalLen = 0;
        int contentLen = 729366;
        int chunkBytes = 64 * 1024;

        while (totalLen < contentLen) {
            byte[] bufTemp = new byte[chunkBytes];
            // 剩余字节数
            int remainLen = contentLen - totalLen;
            // 读取的字节数
            int chunkSize = is.read(bufTemp, 0, (remainLen > chunkBytes) ? chunkBytes : remainLen);
            if (chunkSize <= 0 ){
                break;
            }
            byte[] buf = Arrays.copyOfRange(bufTemp, 0, chunkSize);
            bufTemp = null;
            totalLen += chunkSize;

            int bufIndex = 0;
            while (bufIndex < buf.length) {
                bufIndex = getBoundarySectFromBuf(buf, bufIndex, boundary);
                if (bufIndex < 0) {
                    break;
                }
            }
        }
        return true;
    }

    private static int getBoundarySectFromBuf(byte[] buf, int scanStart, String boundary) throws IOException {
        int scanEnd = -1;
        byte[] scanKey = boundary.getBytes();
        if (buf.length <= 0 || scanKey.length <= 0) {
            return scanEnd;
        }

        int start = indexOf(buf, scanKey, scanStart);
        if (start < 0) {
            // 上一个片段
            scanEnd = appendFileSect(buf) ? buf.length : scanEnd;
            return scanEnd;
        } else if ((start - scanStart) != 2){
            // 不是起点位置
            byte[] tempBuf = Arrays.copyOfRange(buf, 0, start);
            appendFileSect(tempBuf);
            fos.close();
            tempBuf = null;
        }

        String line = getLine(buf, start);
        while (line != null) {
            if (line.indexOf("filename") < 0) {
                start = start + line.getBytes().length + 2;
                line = getLine(buf, start);
                continue;
            }

        }


        return 0;
    }

    private static boolean appendFileSect(byte[] buf) throws IOException {
        fos.write(buf);
        return true;
    }

    @SuppressWarnings("Duplicates")
    private static int indexOf(byte[] buf, byte[] scanKey, int scanStart) {
        int index = 0;
        for (int i = scanStart; i < buf.length - scanKey.length + 1; i++) {
            for (int j = 0; j < scanKey.length; j++) {
                if (scanKey[j] != buf[i + j]) {
                    index = 0;
                    break;
                }
                index++;
            }
            if (index == scanKey.length) {
                return index;
            }
        }
        return -1;
    }

    private static String getLine(byte[] buf, int start) {
        int i = indexOf(buf, "\r\n".getBytes(), start);
        if (i < 0) {
            return null;
        }
        byte[] bytes = Arrays.copyOfRange(buf, start, i + 2);
        return new String(bytes);
    }

}