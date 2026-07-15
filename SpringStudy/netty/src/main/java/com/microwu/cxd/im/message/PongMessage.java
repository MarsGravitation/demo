package com.microwu.cxd.im.message;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/24   15:24
 * Update History:
 * Author        Time            Content
 */
public class PongMessage extends AbstractResponseMessage {
    @Override
    public int getMessageType() {
        return PongMessage;
    }

    @Override
    public String toString() {
        return "PongMessage{}";
    }
}
