package com.microwu.cxd.route;

import com.microwu.cxd.common.util.RabbitmqUtil;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/1   15:27
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class EmitLogDirect {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtil.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String severity = getSeverity("");
        String message = "hello world";

        // 参数2: routeKey
        channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes("UTF-8"));
        System.out.println("Sent message");

    }

    public static String getSeverity(String string) {
        if(string.length() < 1) {
            return "info";
        }
        return "error";
    }

}