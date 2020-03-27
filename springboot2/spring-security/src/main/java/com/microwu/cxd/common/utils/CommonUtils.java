package com.microwu.cxd.common.utils;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/1/14   10:09
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CommonUtils {
    public static String getLastCamelWord(String source) {
        for (int i = source.length() - 1; i >= 0; i--) {
            if (source.toUpperCase().charAt(i) == source.charAt(i)) {
                return source.substring(i).toLowerCase();
            }
        }
        return "";
    }
}