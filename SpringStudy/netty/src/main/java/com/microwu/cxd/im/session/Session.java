package com.microwu.cxd.im.session;

import io.netty.channel.Channel;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/23   10:43
 * Update History:
 * Author        Time            Content
 */
public interface Session {

    void bind(Channel channel, String username);
    void unbind(Channel channel);
    Channel getChannel(String username);

}
