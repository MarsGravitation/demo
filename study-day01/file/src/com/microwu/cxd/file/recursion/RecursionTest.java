package com.microwu.cxd.file.recursion;

import java.io.*;
import java.util.ArrayList;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/9   13:38
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class RecursionTest {

    private static final ArrayList<String> STRINGS = new ArrayList<>();

    private static final String CODE = "MultiReadHttpServletRequest";

    /**
     * 基于路径查找 java 文件的关键字
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/9  13:38
     *
     * @param   	path
     * @return  java.util.List<java.lang.String>
     */
    public static void findNameByCode(String path) throws IOException {
        if (path == null || path.length() == 0) {
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                return;
            }
            for (File f : files) {
                findNameByCode(f.getPath());
            }
        } else {
            // 查找文件中的关键字
            findFromFile(file);
        }

    }

    /**
     * 从文件中查找关键字
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/9  13:49
     *
     * @param   	file
     * @return  void
     */
    private static void findFromFile(File file) throws IOException {
        if (file == null || !file.getPath().endsWith(".java")) {
            return;
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains(CODE)) {
                STRINGS.add(file.getPath());
                break;
            }
        }
        reader.close();
    }

    public static void main(String[] args) throws IOException {
        findNameByCode("E:\\work-note\\Products");
        STRINGS.forEach(System.out::println);
    }

}