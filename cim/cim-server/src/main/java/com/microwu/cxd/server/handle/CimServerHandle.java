package com.microwu.cxd.server.handle;

import com.microwu.cxd.common.constant.Constants;
import com.microwu.cxd.common.pojo.CimUser;
import com.microwu.cxd.common.protocol.CIMRequestProto;
import com.microwu.cxd.common.utils.NettyAttrUtil;
import com.microwu.cxd.server.kit.RouteHandler;
import com.microwu.cxd.server.kit.ServerHeartBeatHandlerImpl;
import com.microwu.cxd.server.utils.SessionSocketHolder;
import com.microwu.cxd.server.utils.SpringBeanFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   9:47
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CimServerHandle extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CimServerHandle.class);

    /**
     * 连接断开
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/10  10:03
     *
     * @param   	ctx
     * @return  void
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        CimUser user = SessionSocketHolder.getUser(ctx.channel());
        if (user != null) {
            LOGGER.warn("[{}] trigger channelInactive offline!", user);

            RouteHandler routeHandler = SpringBeanFactory.getBean(RouteHandler.class);
            routeHandler.userOffLine(user, ctx.channel());

            ctx.channel().close();
        }
    }

    /**
     * 心跳检测
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/10  10:03
     *
     * @param   	ctx
     * @param 		evt
     * @return  void
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                LOGGER.info("定时检测客户端是否存活");

                ServerHeartBeatHandlerImpl heartBeatHandler = SpringBeanFactory.getBean(ServerHeartBeatHandlerImpl.class);
                heartBeatHandler.process(ctx);
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        CIMRequestProto.CIMReqProtocol req = (CIMRequestProto.CIMReqProtocol) msg;
        LOGGER.info("received message = [{}]", req);
        if (req.getType() == Constants.CommandType.LOGIN) {
            // 保存客户端与 Channel 的关系
            SessionSocketHolder.put(req.getRequestId(), ctx.channel());
            SessionSocketHolder.saveSession(req.getRequestId(), req.getReqMsg());
        }

        if (Constants.CommandType.PING == req.getType()) {
            NettyAttrUtil.updateReaderTime(ctx.channel(), System.currentTimeMillis());
            // 向客户端响应 pong 消息
            CIMRequestProto.CIMReqProtocol heartBeat = SpringBeanFactory.getBean(CIMRequestProto.CIMReqProtocol.class);
            ctx.writeAndFlush(heartBeat);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error(cause.getMessage(), cause);
        super.exceptionCaught(ctx, cause);
    }
}