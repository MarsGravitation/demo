package com.microwu.cxd.netty.struct;

import lombok.Data;

/**
 * Description: 消息头
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/26   10:56
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class Header {

    /**
     * 一个 PDU 的总字节数
     * 包括该字段本身的 4 个字节
     */
    private int length;

    /**
     * 命令标识符 - 协议号
     */
    private int commandId;

    /**
     * 用于请求和响应间保持对应关系的序号
     */
    private int transactionID;

    /**
     * 版本号，建议从 1 开始
     */
    private int version = 0x1;

    @Override
    public String toString() {
        return "Header(commandId=" + Integer.toHexString(this.commandId) + ", transactionID=" + Integer.toHexString(this.transactionID) + ", version=" + Integer.toHexString(this.version) + ")";
    }

}