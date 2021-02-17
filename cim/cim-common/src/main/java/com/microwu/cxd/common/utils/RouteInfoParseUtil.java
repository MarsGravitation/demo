package com.microwu.cxd.common.utils;

import com.microwu.cxd.common.pojo.RouteInfo;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   15:03
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class RouteInfoParseUtil {

    public static RouteInfo parse(String info) {
        String[] split = info.split(":");
        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setIp(split[0]);
        routeInfo.setCimServerPort(Integer.valueOf(split[1]));
        routeInfo.setHttpPort(Integer.valueOf(split[2]));
        return routeInfo;
    }

}