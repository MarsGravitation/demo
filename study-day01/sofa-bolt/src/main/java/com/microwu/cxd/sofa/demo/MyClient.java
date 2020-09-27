package com.microwu.cxd.sofa.demo;

import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcClient;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/24   9:39
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MyClient {

    private static RpcClient client;

    public static void start() throws RemotingException, InterruptedException {
        client = new RpcClient();

        client.init();

    }

    public static void main(String[] args) throws RemotingException, InterruptedException {
        MyClient.start();

        MyRequest request = new MyRequest();
        request.setReq("hello, bolt-server");

        MyResponse response = (MyResponse) client.invokeSync("127.0.0.1:8888", request, 30 * 1000);
        System.out.println(response);
    }

}