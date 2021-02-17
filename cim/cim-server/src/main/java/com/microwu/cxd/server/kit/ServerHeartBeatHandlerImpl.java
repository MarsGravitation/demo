package com.microwu.cxd.server.kit;

import com.microwu.cxd.common.kit.HeartBeatHandler;
import com.microwu.cxd.common.pojo.CimUser;
import com.microwu.cxd.common.utils.NettyAttrUtil;
import com.microwu.cxd.server.properties.CimServerProperties;
import com.microwu.cxd.server.utils.SessionSocketHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   10:11
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class ServerHeartBeatHandlerImpl implements HeartBeatHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ServerHeartBeatHandlerImpl.class);

    @Autowired
    private CimServerProperties properties;

    @Autowired
    private RouteHandler routeHandler;

    @Override
    public void process(ChannelHandlerContext ctx) throws Exception {
        long heartBeatTime = properties.getHeartBeatTime() * 1000;

        Long lastReadTime = NettyAttrUtil.getReaderTime(ctx.channel());
        long now = System.currentTimeMillis();
        if (lastReadTime != null && now - lastReadTime > heartBeatTime) {
            CimUser user = SessionSocketHolder.getUser((NioSocketChannel) ctx.channel());
            if (user != null) {
                LOGGER.warn("客户端[{}]心跳超时[{}]ms, 需要关闭连接", user, now - lastReadTime);
            }
            routeHandler.userOffLine(user, ctx.channel());
            ctx.channel().close();
        }
    }
}