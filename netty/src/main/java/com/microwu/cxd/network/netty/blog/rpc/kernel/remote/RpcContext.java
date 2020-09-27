package com.microwu.cxd.network.netty.blog.rpc.kernel.remote;

import java.io.Serializable;

/**
 * Description: RPC 请求上下文对象
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   15:06
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class RpcContext implements Serializable {

    private static final long serialVersionUID = -8581999645641203512L;

    private String className;
    private String methodName;
    private Class[] types;
    private Object[] params;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getTypes() {
        return types;
    }

    public void setTypes(Class[] types) {
        this.types = types;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

}