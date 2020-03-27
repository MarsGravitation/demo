package com.microwu.cxd.simple_factory;

/**
 * Description:
 *  实现：
 *      1.创建一个Shape接口
 *      2.创建Shape的实现类
 *      3.定义ShapeFactory,定义一个静态方法，传入一个type，根据type获取对应的对象
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/3   14:10
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class FactoryPatternDemo {
    public static void main(String[] args) {
        // 获取Circle对象
        Shape shape = ShapeFactory.getShage("Circle");
        shape.draw();

    }

}