package com.microwu.coding;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Description: 放弃思考，编码问题再也不思考了
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/27   16:35
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CodingTest {

    public static void main(String[] args) throws UnsupportedEncodingException {
        // 获取默认编码 - 这里的默认的编码是文件的编码
        System.out.println(System.getProperty("file.encoding"));
        System.out.println(Charset.defaultCharset().name());

        // 此方法使用默认的编码方式

        /**
         * 出现乱码的情况：
         *  文件是GBK编码，而 str 是 UTF-8，编译不通过
         *
         *  当时我是把 UTF编码的字符 copy 到 GBK的文件下
         *
         *
         */
        String str = "成旭东";
        // 以 UTF-8 编码
        byte[] bytes = str.getBytes();
        System.out.println(new String(bytes));

        printBytes(getBytesWithDefaultEncoding(str));

        printBytes(getBytesWithGivenEncoding(str, "gbk"));

    }

    /**
     * Gets the bytes with default encoding.
     *
     * @param content the content
     *
     * @return the bytes with default encoding
     */
    public static byte[] getBytesWithDefaultEncoding(String content) {
        System.out.println("\nEncode with default encoding\n");
        byte[] bytes = content.getBytes();
        return bytes;
    }

    /**
     * Gets the bytes with given encoding.
     *
     * @param content the content
     * @param encoding the encoding
     *
     * @return the bytes with given encoding
     */
    public static byte[] getBytesWithGivenEncoding(String content, String encoding) {
        System.out.println("\nEncode with given encoding : " + encoding + "\n");
        try {
            byte[] bytes = content.getBytes(encoding);
            return bytes;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Prints the bytes.
     *
     * @param bytes the bytes
     */
    public static void printBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            System.out.print(" byte[" + i + "] = " + bytes[i]);
            System.out
                    .println(" hex string = " + Integer.toHexString(bytes[i]));
        }
    }
}