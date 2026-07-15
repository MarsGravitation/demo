package com.microwu.cxd.network.netty.im.session;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/23   15:10
 * Update History:
 * Author        Time            Content
 */
public class SessionFactory {

    private static Session session = new SessionMemoryImpl();

    public static Session getSession() {
        return session;
    }

}
