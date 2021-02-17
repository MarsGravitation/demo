package com.microwu.cxd.server.utils;

import com.microwu.cxd.common.pojo.CimUser;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   10:21
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SessionSocketHolder {

    private static final Map<Long, Channel> CHANNEL_MAP = new ConcurrentHashMap<>(16);
    private static final Map<Long, String> SESSION_MAP = new ConcurrentHashMap<>(16);

    public static void saveSession(Long userId, String username) {
        SESSION_MAP.put(userId, username);
    }

    public static void removeSession(Long userId) {
        SESSION_MAP.remove(userId);
    }

    public static void put(Long id, Channel channel) {
        CHANNEL_MAP.put(id, channel);
    }

    public static Channel get(Long id) {
        return CHANNEL_MAP.get(id);
    }

    public static Map<Long, Channel> getRelationShip() {
        return CHANNEL_MAP;
    }

    public static void remove(Channel channel) {
        CHANNEL_MAP.entrySet().stream().filter(entry -> entry.getValue() == channel).forEach(entry -> CHANNEL_MAP.remove(entry.getKey()));
    }

    public static CimUser getUser(Channel channel) {
        for (Map.Entry<Long, Channel> entry : CHANNEL_MAP.entrySet()) {
            Channel value = entry.getValue();
            if (channel == value) {
                Long key = entry.getKey();
                String username = SESSION_MAP.get(key);
                CimUser cimUser = new CimUser();
                cimUser.setUserId(key);
                cimUser.setUsername(username);
                return cimUser;
            }
        }
        return null;
    }

}