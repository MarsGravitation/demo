package com.microwu.cxd.network.netty.blog.rpc.consumer;

import com.microwu.cxd.network.netty.blog.rpc.client.Param;
import com.microwu.cxd.network.netty.blog.rpc.client.QueryStudentClient;
import com.microwu.cxd.network.netty.blog.rpc.client.Result;
import com.microwu.cxd.network.netty.blog.rpc.client.StudentBean;
import com.microwu.cxd.network.netty.blog.rpc.kernel.remote.RpcProxyFactory;

import java.util.Scanner;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   15:54
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ConsumerService {

    public void queryStudent(String name) {
        RpcProxyFactory<Object> factory = new RpcProxyFactory<>();

        QueryStudentClient client = (QueryStudentClient) factory.factoryRemoteInvoker("localhost", 8080, QueryStudentClient.class);

        Param param = new Param();
        param.setName(name);

        Result<StudentBean> result = client.query(param);
        if (result != null && result.isSuccess()) {
            StudentBean outBean = result.getData();
            System.out.println("姓名:" + outBean.getName());
            System.out.println("性别:" + outBean.getSex());
            System.out.println("年龄:" + outBean.getAge());
            System.out.println("尺寸:" + outBean.getSize());
        } else {
            System.out.println(param.getName() + "查无此人");
        }
    }

    public static void main(String[] args) {
        ConsumerService consumerService = new ConsumerService();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String string = scanner.next();
            consumerService.queryStudent(string);
        }
    }
}