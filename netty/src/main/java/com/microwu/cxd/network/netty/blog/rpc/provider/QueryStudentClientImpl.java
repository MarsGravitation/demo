package com.microwu.cxd.network.netty.blog.rpc.provider;

import com.microwu.cxd.network.netty.blog.rpc.client.Param;
import com.microwu.cxd.network.netty.blog.rpc.client.QueryStudentClient;
import com.microwu.cxd.network.netty.blog.rpc.client.Result;
import com.microwu.cxd.network.netty.blog.rpc.client.StudentBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   15:51
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class QueryStudentClientImpl implements QueryStudentClient {

    private static Map<String, StudentBean> studentBeanMap = new HashMap<>();

    @Override
    public Result<StudentBean> query(Param param) {
        Result<StudentBean> result = new Result<>();

        StudentBean bean = studentBeanMap.get(param.getName());
        if (bean != null) {
            result.setSuccess(true);
            result.setData(bean);
        } else {
            result.setSuccess(false);
            result.setFailCode("500");
        }
        return result;
    }
    
    static {
        StudentBean t1=new StudentBean();
        t1.setName("fox");
        t1.setAge(66);
        t1.setSex("man");
        t1.setSize(175);

        StudentBean t2=new StudentBean();
        t2.setName("monkey");
        t2.setAge(66);
        t2.setSex("man");
        t2.setSize(170);

        studentBeanMap.put("fox",t1);
        studentBeanMap.put("monkey",t2);
    }
}