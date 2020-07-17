package com.microwu.cxd.file.code;

import java.io.*;

/**
 * Description: 文件转码
 *
 * https://www.cnblogs.com/qinqianyu/p/11418518.html
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/28   9:46
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ChangeFileEncoding {

    /**
     * 将文件 path 的 originCoding 编码转为 targetCoding 编码
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/28  9:49
     *
     * @param   	path
     * @param 		originCoding
     * @param 		targetCoding
     * @return  void
     */
    public static void convert(String path, String originCoding, String targetCoding) throws IOException {

        // 1. 将路径信息转换成文件
        File file = new File(path);
        String fileName = file.getName();
        String filePath = file.getPath();

        String targetFileName = fileName.replace(".", "-" + targetCoding + ".");
        String targetFilePath = filePath.substring(0, filePath.lastIndexOf(File.separator)) + File.separator + targetFileName;

        BufferedReader reader;
        BufferedWriter writer;

        // 2. 将路径信息转换成 reader
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), originCoding));
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFilePath), targetCoding));

        // 3. 转换
        StringBuffer sb = new StringBuffer();
        String str;
        while ((str = reader.readLine()) != null) {
            sb.append(str);
            writer.write(sb.toString());
            // 换行
            writer.newLine();
            // 清除本行内容
            sb.setLength(0);
        }

        writer.close();
        reader.close();
    }

    public static void main(String[] args) throws IOException {
        convert("C:\\Users\\Administration\\Desktop\\新建文件夹\\运营任务\\王钻\\日报\\200716_detailedlist.csv", "UTF-8", "GBK");
    }

}