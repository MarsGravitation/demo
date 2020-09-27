package com.microwu.cxd.netty.server.action;

import com.microwu.cxd.netty.server.action.param.Param;
import com.microwu.cxd.netty.server.action.res.WorkRes;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/10   15:14
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public interface WorkAction {

    WorkRes execute(Param param) throws Exception;
}