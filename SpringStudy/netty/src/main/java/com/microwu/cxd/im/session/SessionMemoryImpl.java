package com.microwu.cxd.im.session;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/23   10:44
 * Update History:
 * Author        Time            Content
 */
public class SessionMemoryImpl implements Session {

    private final Map<String, Channel> usernameChannelMap = new ConcurrentHashMap<>();
    private final Map<Channel, String> channelUsernameMap = new ConcurrentHashMap<>();

    @Override
    public void bind(Channel channel, String username) {
        usernameChannelMap.put(username, channel);
        channelUsernameMap.put(channel, username);
//        channelAttributesMap.put(channel, new ConcurrentHashMap<>());
    }

    @Override
    public void unbind(Channel channel) {
        String username = channelUsernameMap.remove(channel);
        usernameChannelMap.remove(username);
//        channelAttributesMap.remove(channel);
    }

    @Override
    public Channel getChannel(String username) {
        return usernameChannelMap.get(username);
    }
}
