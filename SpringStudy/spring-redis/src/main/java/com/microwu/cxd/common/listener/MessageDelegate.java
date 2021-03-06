package com.microwu.cxd.common.listener;

import java.io.Serializable;
import java.util.Map;

/**
 * Description:
 *
 * @Author: Administration             chengxudong@microwu.com
 * Date:           2020/7/13   16:23
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */

public interface MessageDelegate {
    void handleMessage(String message);
    void handleMessage(Map message);
    void handleMessage(byte[] message);
    void handleMessage(Serializable message);
    void handleMessage(Serializable message, String channel);
}
