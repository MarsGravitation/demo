package com.microwu.cxd.tool.log;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/18   10:34
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SimpleRecorderFilter extends HttpBodyRecorderFilter {
    @Override
    protected void recordBody(String payload, String response) {
        System.out.println(payload);
        System.out.println(response);
    }

    @Override
    protected String recordCode() {
        return "200,300,400,500";
    }
}