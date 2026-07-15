package com.microwu.cxd.im.session;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/24   10:37
 * Update History:
 * Author        Time            Content
 */
public class GroupSessionFactory {

    private static GroupSession session = new GroupSessionMemoryImpl();

    public static GroupSession getSession() {
        return session;
    }

}
