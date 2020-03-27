package com.microwu.cxd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/22   15:04
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class OutputService {
    public void output(String fileName, String a, String b) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(new File(fileName));
        printWriter.write(a + ":" + b);
        printWriter.close();

    }
}