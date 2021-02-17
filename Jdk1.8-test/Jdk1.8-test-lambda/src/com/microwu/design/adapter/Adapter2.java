package com.microwu.design.adapter;

/**
 * Description: 对象适配
 *  对象适配通过组合被适配的类的引用实现
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/26   13:54
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Adapter2 implements ITarget {

    private Adaptee adaptee;

    public Adapter2(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void doTargetRequest() {
        // 这里可以写一些业务代码，如果有参数，就是对参数的转换

        // 实现了对既有代码的复用，通过 Adapter，把适配者的 API - request，适配给了目标接口 - doTargetRequest
        this.adaptee.request();

        // 这里也可以写一些业务代码

    }
}