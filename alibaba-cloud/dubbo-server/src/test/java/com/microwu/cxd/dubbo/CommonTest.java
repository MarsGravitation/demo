package com.microwu.cxd.dubbo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/27   17:27
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CommonTest {
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}):?(\\d{1,5})?");
        Matcher matcher = pattern.matcher("192.168.23.1:-1");
        if (matcher.matches()) {
            System.out.println("匹配");
        } else {
            System.out.println("不匹配");
        }
    }
}