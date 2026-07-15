package com.microwu.cxd.im.session;

import io.netty.channel.Channel;

import java.util.List;
import java.util.Set;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/24   10:30
 * Update History:
 * Author        Time            Content
 */
public interface GroupSession {

    Group createGroup(String name, Set<String> members);

    Group joinMember(String name, String member);

    Group removeMember(String name, String member);

    Group removeGroup(String name);

    Set<String> getMembers(String name);

    List<Channel> getMemberChannel(String name);
}
