package com.microwu.cxd.im.message;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/24   15:23
 * Update History:
 * Author        Time            Content
 */
public class PingMessage extends Message {
    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
