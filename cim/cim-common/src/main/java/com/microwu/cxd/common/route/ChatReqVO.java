package com.microwu.cxd.common.route;

import com.microwu.cxd.common.req.BaseRequest;
import lombok.Data;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   14:26
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class ChatReqVO extends BaseRequest {

    private Long userId;

    private String message;

}