package com.microwu.cxd.sofa.demo;

import com.alipay.remoting.BizContext;
import com.alipay.remoting.rpc.protocol.SyncUserProcessor;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/24   9:35
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MyServerUserProcessor extends SyncUserProcessor<MyRequest> {
    @Override
    public Object handleRequest(BizContext bizContext, MyRequest myRequest) throws Exception {
        MyResponse response = new MyResponse();
        if (myRequest != null) {
            System.out.println(myRequest);
            response.setResp("from server -> " + myRequest.getReq());
        }
        return response;
    }

    @Override
    public String interest() {
        return MyRequest.class.getName();
    }
}