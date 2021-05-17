package com.microwu.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

/**
 * File
 *
 * 构造函数
 *
 * public File(String pathname) // 文件的绝对路径
 * public File(URI uri) // 文件的 URI 地址
 *
 * public File(String parent, String child) // 指定父文件绝对路径、子文件绝对路径
 * public File(File parent, String child) // 指定父文件、子文件相对路径
 *
 * boolean file.mkdir() // 创建目录
 * boolean file.mkdirs() // 创建多级目录
 *
 * boolean file.createNewFile() // 创建一个新的文件
 *
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/5/10  13:41
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class FileTest {

    public static void main(String[] args) {
        FileTest test = new FileTest();

        test.test();
    }

    public void test() {
        String filePath = "C:\\Users\\issuser\\Desktop\\新建文件夹\\note\\操作系统";
        File file = new File(filePath);
        file.mkdir();

        String[] names = file.list();
        for (int i = 0; i < names.length; i++) {
            System.out.println("names: " + names[i]);
        }

        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.println("file: " + files[i].getAbsolutePath());
        }
    }

    /**
     * 扫描 F 盘所有的文件
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/5/10     13:50
     *
     * @param
     * @return void
     */
    public void test02(File file) {
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isHidden()) {
                continue;
            }

            if (f.isDirectory()) {
                test02(f);
            } else {
                System.out.println(f.getAbsolutePath() + " " + f.getName());
            }
        }
    }

    /**
     * 获取指定目录的所有文件夹
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/5/10     13:54
     *
     * @param file
     * @return void
     */
    public void test03(File file) {
        MyFileFilter filter = new MyFileFilter();

        File[] files = file.listFiles(filter);
        for (File f : files) {
            if (f.isHidden()) {
                continue;
            }

            System.out.println(f.getAbsolutePath());
        }
    }

    /**
     * 文件过滤功能
     */
    class MyFileFilter implements FileFilter {

        @Override
        public boolean accept(File pathname) {
            if (pathname.isDirectory()) {
                return true;
            }
            return false;
        }
    }

    /**
     * 文件名字过滤功能
     */
    class MyFilenameFilter implements FilenameFilter {

        private String type;
        MyFilenameFilter(String type) {
            this.type = type;
        }

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(type);
        }
    }

}
