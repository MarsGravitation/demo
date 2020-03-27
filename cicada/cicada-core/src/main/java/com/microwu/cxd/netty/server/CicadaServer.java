package com.microwu.cxd.netty.server;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/19   11:33
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CicadaServer {

    /**
     * Start cicada server by path
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/12/19  11:34
     *
     * @param   	clazz
     * @param 		path
     * @return  void
     */
    public static void start(Class<?> clazz, String path) throws Exception {
        CicadaSetting.setting(clazz, path);
        NettyBootStrap.startCicada();
    }

    public static void start(Class<?> clazz) throws Exception {
        start(clazz, null);
    }
}