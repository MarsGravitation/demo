package com.microwu.cxd.netty.server.utils;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/10   15:28
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class PathUtils {

    public static String getRootPath(String path) {
        return "/" + path.split("/")[1];
    }

    public static String getActionPath(String path) {
        return path.split("/")[2];
    }
}