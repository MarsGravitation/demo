package com.microwu.design.adapter;

/**
 * Description: 类的适配
 *  通过继承被适配者 - Adaptee，把被适配者的 API - request 适配给目标接口 - ITarget 的 API - doTargetRequest
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/26   13:50
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Adapter extends Adaptee implements ITarget {
    @Override
    public void doTargetRequest() {
        // 这里可以写一些业务代码，如果由参数，就是对参数的转换过程

        // 通过 Adapter，把适配者的 API - request，适配给了目标接口，doTargetRequest，实现了对既有代码的复用
        super.request();

        // 这里可以写一些业务代码
    }
}