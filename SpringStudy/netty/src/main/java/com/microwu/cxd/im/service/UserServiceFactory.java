package com.microwu.cxd.im.service;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/23   15:08
 * Update History:
 * Author        Time            Content
 */
public class UserServiceFactory {

    private static UserService userService = new UserServiceMemoryImpl();

    public static UserService getUserService() {
        return userService;
    }

}
