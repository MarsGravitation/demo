package com.microwu.io;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * BufferedInputStream 和 BufferedOutputStream 分别是 FileInputStream 和 FileOutputStream 的子类，
 * 作为装饰器子类，使用它们可以防止每次读取/发送数据时进行实际的写操作，代表缓冲区
 *
 * 每读一个字节就要写入一个字节，由于涉及磁盘的 IO 操作相比内存的操作要慢很多，所以不带缓冲的流
 * 效率很低。带缓冲的流，可以一次读很多字节，但不向磁盘中歇入明知是先放到内存里。等凑够了缓冲区大小的
 * 时候一次性写入磁盘，这种方式可以减少磁盘操作次数，速度就会提高很多。
 *
 * 同时正因为它们实现了缓冲功能，所以要注意使用 BufferedOutputStream 写完数据后，要调用 flush/close方法，
 * 强行将缓冲区的数据写出。
 *
 * flush
 *  当 OutputStream 是 BufferedOutputStream 时需要 flush。
 *  buffer 其实就是一个 byte[]，BufferedOutputStream 的每一次 write 其实是将内容写入 byte[]，当 buffer 容量达到上限时，
 *  会触发真正的磁盘写入
 *  另一种触发磁盘写入的方法就是调用 flush
 *  close 会自动 flush
 *  不调用 close，缓冲区不满，有需要把缓冲区的内容写入到文件或通过网络发送时，需要 flush
 *
 * 如何正确的关流
 *  先关闭外层流，再关闭内层流。一般情况下是：先打开的后关闭，后打开的先关闭
 *  或者看依赖关系，如果 a 依赖 b，先关闭 a，再关闭 b。例如处理流 a 依赖节点流 b，
 *  先关闭流 a，再关闭节点流 b
 *
 * close 的作用
 *  |- 关闭输入流，并且释放系统资源
 *  |- BufferedInputStream 只需要关闭本身即可，它最终会调用真正数据源对象的 close 方法
 *  因此，可以只调用外层流的 close 方法关闭其修饰的内层流
 *
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/5/10  14:01
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class BufferedIO {

    public void copyFile(File oldFile, File newFile) {
        InputStream inputStream = null;
        BufferedInputStream bis = null;

        OutputStream outputStream = null;
        BufferedOutputStream bos = null;

        try {
            inputStream = new FileInputStream(oldFile);
            bis = new BufferedInputStream(inputStream);

            outputStream = new FileOutputStream(newFile);
            bos = new BufferedOutputStream(outputStream);

            // 代表一次最多赌气 1kb 的内容
            byte[] b = new byte[1024];
            // 代表实际读取的字节数
            int length = 0;
            while ((length = bis.read(b)) != -1) {
                // length 代表实际读取的字节数
                bos.write(b, 0, length);
            }
            // 缓冲区的内容写入到文件
            bos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (outputStream != null) {
//                try {
//                    outputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

    /**
     * 复制文本文件
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/5/10     14:52
     *
     * @param oldFile
     * @param newFile
     * @return void
     */
    public void copyTxtFile(File oldFile, File newFile) {
        Reader reader = null;
        BufferedReader bufferedReader = null;

        Writer writer = null;
        BufferedWriter bufferedWriter = null;

        try {
            reader = new FileReader(oldFile);
            bufferedReader = new BufferedReader(reader);

            writer = new FileWriter(newFile);
            bufferedWriter = new BufferedWriter(writer);

            // 每次读取一行的内容
            String result = null;
            while ((result = bufferedReader.readLine()) != null) {
                // 把内容写入文件
                bufferedWriter.write(result);
                // 换行
                bufferedWriter.newLine();
            }
            // 强制把数组内容写入文件
            bufferedWriter.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * ByteArrayInputStream 可以将字节数组转化为输入流
     * ByteArrayOutputStream 可以捕获内存缓冲区的数据，转换成字节数组
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/5/10     14:59
     *
     * @param
     * @return void
     */
    public void test() {
        String mes = "hello world";
        byte[] bytes = mes.getBytes(StandardCharsets.UTF_8);

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        int result;
        while((result = bis.read()) != -1) {
            System.out.println((char) result);
        }

        try {
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bos.write(bytes);

            FileOutputStream fileOutputStream = new FileOutputStream("");
            bos.writeTo(fileOutputStream);
            fileOutputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
