package com.microwu.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 *
 * IO 流的分类：
 *  |- 字符流和字节流
 *  |- 输入流和输出流
 *
 * 字符流和字节流
 *  因为数据编码的不同，而有了对字符进行高效操作的刘对象。本质其实就是基于字节流读取时，去查了指定的码表。区别：
 *      |- 读写单位不同，字节流一个字节（8 bit）为单位，字符流以字符为单位，根据码表映射字符，一次可能读取多个字节
 *      |- 处理对象不同，字节流能处理所有类型的数据，而字符流只能处理字符类型的数据
 *      |- 字节流：一次读入或读出是八位二进制
 *      |- 字符流，一次读入或读出是十六位二进制
 *  设备上的数据无论是图片或者视频，文字，他们都是以二进制存储的。二进制的最终都是以一个 8 位为
 *  数据单元进行体现，所以计算机中最小数据单元就是字节。意味着，字节流可以处理设备上的所有数据，
 *  所以字节流一样可以处理字符数据
 *
 *  结论：只要是处理纯文本数据，就优先考虑使用字符流。除此之外都是用字节流
 *
 * 输入流和输出流
 *  输入字节流 InputStream
 *      |- InputStream 是所有的输入字节流的父类，他是一个抽象类
 *      |- ByteArrayInputStream、StringBufferInputStream、FileInputStream 是三种基本的介质流，
 *      它们分别从 Byte 数组中、StringBuffer、本地文件中读取数据
 *  输出字节流：
 *      |- OutputStream 是所有输出字节流的父类，它是一个抽象类
 *      |- ByteArrayOutputStream、FileOutputStream 是两种基本的介质流，他们分别向 Byte 数组
 *      和本地文件中写入数据
 *
 *  总结：
 *      输入流：InputStream/Reader：从文件中读取到程序中
 *      输出流：OutputStream/Writer：从程序中输出到文件中
 *
 * 节点流：
 *  父类：InputStream、OutputStream、Reader、Writer
 *  文件：FileInputStream、FileOutputStream、FileReader、FileWriter
 *  数组：ByteArrayInputStream、ByteArrayOutputStream、CharArrayReader、CharArrayWriter
 *  处理流：处理流和节点流一块使用，在节点流的基础上，再套一层。直接使用节点流，读写不方便，为了更快、更方便的读写文件，才有了处理流
 *  缓冲流：BufferedInputStream、BufferedOutputStream、BufferedReader、BufferedWriter
 *  转换流：InputStreamReader、OutputStreamReader 实现字节流和字符流之间的转换
 *  数据流：DataInputStream、DataOutputStream 将基础数据类型写入或读取出来
 *
 *  注意：
 *      |- 在实际的项目中，所有的 IO 操作都应该放到子线程中操作，避免堵住主线程
 *
 *
 * https://blog.csdn.net/zhaoyanjun6/article/details/54292148
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/5/10  9:43
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class IOTest {

    public static void main(String[] args) {
        IOTest test = new IOTest();

        String s = test.readFile("C:\\Users\\issuser\\Desktop\\新建文件夹\\note\\操作系统\\linux.txt");
        System.out.println(s);
    }

    /**
     * 读取指定文件的内容
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/5/10     10:16
     *
     * @param filePath
     * @return java.lang.String
     */
    public String readFile(String filePath) {
        FileInputStream fis = null;
        String result = null;
        try {
            // 1. 根据 path 路径实例化一个输入流对象
            fis = new FileInputStream(filePath);

            // 2. 返回这个输入流中可以被堵的剩下的 bytes 字节的估计值
            int size = fis.available();
            // 3. 根据输入流中的字节数创建 byte 数组
            byte[] array = new byte[size];
            // 4. 把数据读入数组中
            fis.read(array);
            // 5. 根据获取到的 bytes 数组新建一个字符串
            result = new String(array);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    
    /**
     * 写文件
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/5/10     10:23
     *
     * @param filePath
     * @param content
     * @return void
     */
    public void writeFile(String filePath, String content) {
        FileOutputStream fos = null;
        try {
            // 1. 根据文件路径创建输出流
            fos = new FileOutputStream(filePath);
            // 2. 把 String 转换为 byte 数组
            byte[] array = content.getBytes(StandardCharsets.UTF_8);
            // 3. 把 byte 数组输出
            fos.write(array);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void copyFile(String filePathOld, String filePathNew) {
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            // 1. 根据 path 路径实例化一个输入流的对象
            fis = new FileInputStream(filePathOld);
            // 2. 返回这个输入流综合那个可以被读的剩下的 bytes 字节的估计值
            int size = fis.available();
            // 3. 根据输入流中的字节数创建 byte 数组
            byte[] array = new byte[size];
            // 4. 把数据读取到数组中
            fis.read(array);
            // 5. 把 byte 数组输出
            fos.write(array);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
