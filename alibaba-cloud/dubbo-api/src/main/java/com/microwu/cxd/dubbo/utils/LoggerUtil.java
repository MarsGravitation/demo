package com.microwu.cxd.dubbo.utils;

import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/28   15:07
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LoggerUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerUtil.class);

    public static void log(String url, Object object) {
        String message = String.format("the client[%s] use %s protocol to call %s : %s",
                RpcContext.getContext().getRemoteAddress(), RpcContext.getContext().getUrl().getProtocol(),
                url, object);

        LOGGER.info(message);

    }
}