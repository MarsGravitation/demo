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

    /**
     * 				Class<?>[] types = m.getParameterTypes();
     * 				Object[] args = //
     * 						types.length == 2 //
     * 								&& types[0].isInstance(arguments[0]) //
     * 								&& types[1].isInstance(arguments[1]) ? arguments : message;
     *
     * 				if (!types[0].isInstance(args[0])) {
     * 					continue;
     *                                }
     *
     * isInstance 这个对象能不能转换为这个类
     *
     * 1. 会最先匹配两个参数的方法且参数类型匹配
     * 2. 且第一个参数类型匹配
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/9/1  17:20
     *
     * @param   	message
     * @param 		channel
     * @return  void
     */
    @Override
    public void handleMessage(Serializable message, String channel) {
        System.out.println(message + " " + channel);
    }
}