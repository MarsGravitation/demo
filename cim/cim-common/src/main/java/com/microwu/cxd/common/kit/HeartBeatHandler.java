package com.microwu.cxd.common.kit;

import io.netty.channel.ChannelHandlerContext;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   10:17
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public interface HeartBeatHandler {

    /**
     * 处理心跳
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/10  10:18
     *
     * @param   	ctx
     * @return  void
     */
    void process(ChannelHandlerContext ctx) throws Exception;

}