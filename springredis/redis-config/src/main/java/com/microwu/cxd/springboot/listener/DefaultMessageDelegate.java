package com.microwu.cxd.springboot.listener;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/13   14:55
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class DefaultMessageDelegate implements  MessageDelegate {

    @Override
    public void handleMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void handleMessage(Map message) {

    }

    @Override
    public void handleMessage(byte[] message) {

    }

    @Override
    public void handleMessage(Serializable message, String channel) {

    }
}