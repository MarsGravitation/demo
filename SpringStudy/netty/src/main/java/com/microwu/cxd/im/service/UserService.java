package com.microwu.cxd.im.service;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/23   10:07
 * Update History:
 * Author        Time            Content
 */
public interface UserService {

    /**
     * login
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2023/2/23 10:08
     *
     * @param: username - [java.lang.String]
     * @param: password - [java.lang.String]
     * @return: boolean
     */
    boolean login(String username, String password);

}
