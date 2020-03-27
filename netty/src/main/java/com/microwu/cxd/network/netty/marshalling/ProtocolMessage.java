package com.microwu.cxd.network.netty.marshalling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/20   9:28
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;

    private String code;

    private String content;
}