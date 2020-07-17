package com.microwu.cxd.common.listener;

import java.io.Serializable;
import java.util.Map;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/13   16:23
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class DefaultMessageDelegate implements MessageDelegate {
    @Override
    public void handleMessage(String message) {
        System.out.println("string");
    }

    @Override
    public void handleMessage(Map message) {
        System.out.println("map");
    }

    @Override
    public void handleMessage(byte[] message) {
        System.out.println("byte");
    }

    /**
     * 这里走的是这个方法，适配器会自动使用 RedisSerializer 进行底层格式和所需对象类型之间转换
     * 测试方法转换成了 String
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/13  16:39
     *
     * @param   	message
     * @return  void
     */
    @Override
    public void handleMessage(Serializable message) {
        System.out.println("serializable");
        System.out.println(message);
    }

    @Override
    public void handleMessage(Serializable message, String channel) {
        System.out.println("message + channel");
    }
}