package com.microwu.cxd.network.netty.blog.rpc.kernel.registry;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   15:05
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Registry {

    public static ConcurrentHashMap<String, Class> map = new ConcurrentHashMap<>();

}