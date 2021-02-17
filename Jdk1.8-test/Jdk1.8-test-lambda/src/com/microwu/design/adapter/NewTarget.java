package com.microwu.design.adapter;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/26   13:49
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class NewTarget implements ITarget {
    @Override
    public void doTargetRequest() {
        System.out.println("NewTarget 的目标方法：doRequest");
    }
}