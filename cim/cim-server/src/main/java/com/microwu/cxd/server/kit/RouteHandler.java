package com.microwu.cxd.server.kit;

import com.microwu.cxd.common.pojo.CimUser;
import com.microwu.cxd.server.properties.CimServerProperties;
import com.microwu.cxd.server.utils.SessionSocketHolder;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   10:44
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class RouteHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteHandler.class);

    @Autowired
    private CimServerProperties properties;

    public void userOffLine(CimUser user, Channel channel) {
        if (user != null) {
            LOGGER.info("Account [{}] offline", user);
            SessionSocketHolder.removeSession(user.getUserId());
        }

        SessionSocketHolder.remove(channel);
    }

}