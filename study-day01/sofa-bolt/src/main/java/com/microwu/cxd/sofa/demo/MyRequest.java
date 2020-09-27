package com.microwu.cxd.sofa.demo;

import java.io.Serializable;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/24   9:32
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MyRequest implements Serializable {

    private static final long serialVersionUID = -7242884346498114969L;

    private String req;

    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }
}