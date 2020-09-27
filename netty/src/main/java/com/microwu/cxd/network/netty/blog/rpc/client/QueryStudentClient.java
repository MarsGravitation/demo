package com.microwu.cxd.network.netty.blog.rpc.client;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   15:48
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public interface QueryStudentClient {
    Result<StudentBean> query(Param param);
}