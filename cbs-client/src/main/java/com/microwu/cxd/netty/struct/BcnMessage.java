package com.microwu.cxd.netty.struct;

import lombok.Data;

/**
 * Description: 业务管控消息（Business control news）
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/26   10:55
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class BcnMessage {

    /**
     * 消息头
     */
    private Header header;

    /**
     * 消息体
     */
    private Body body;

}