package com.microwu.cxd.netty;

import com.microwu.cxd.netty.client.CbsClient;
import com.microwu.cxd.netty.core.Address;
import com.microwu.cxd.netty.handler.HeartbeatHandler;

import static com.microwu.cxd.netty.enumx.CommandIDEnum.ENQUIRE_LINK_REQ;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/29   8:23
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class NettyApplication {

    public static void main(String[] args) {
        CbsClient cbsClient = new CbsClient();
        cbsClient.start();

        Address address = new Address();
        address.setIp("180.153.186.153");
        address.setPort(9000);
        cbsClient.oneWay(address, HeartbeatHandler.buildLinkReq(ENQUIRE_LINK_REQ));
    }
}