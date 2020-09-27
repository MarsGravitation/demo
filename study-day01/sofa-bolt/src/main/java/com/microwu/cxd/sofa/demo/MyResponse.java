package com.microwu.cxd.sofa.demo;

import java.io.Serializable;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/24   9:33
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MyResponse implements Serializable {

    private static final long serialVersionUID = -6215194863976521002L;

    private String resp = "default resp from server";

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }
}