package com.microwu.cxd.network.netty.im.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/23   10:09
 * Update History:
 * Author        Time            Content
 */
public class UserServiceMemoryImpl implements UserService {

    private Map<String, String> allUserMap = new ConcurrentHashMap<>();

    {
        allUserMap.put("cxd", "123456");
        allUserMap.put("cxj", "123456");
    }

    /**
     * login
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2023/2/23 10:10
     *
     * @param: username - [java.lang.String]
     * @param: password - [java.lang.String]
     * @return: boolean
     */
    @Override
    public boolean login(String username, String password) {
        String pass = allUserMap.get(username);
        if (pass == null) {
            return false;
        }
        return pass.equals(password);
    }
}
