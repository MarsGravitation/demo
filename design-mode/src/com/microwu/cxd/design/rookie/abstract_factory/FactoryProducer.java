package com.microwu.cxd.design.rookie.abstract_factory;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/23   14:33
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class FactoryProducer {
    public static AbstractFactory getFactory(String choice) {
        if(choice.equalsIgnoreCase("SHAPE")) {
            return new ShapeFactory();
        } else if(choice.equalsIgnoreCase("COLOR")) {
            return new ColorFactory();
        }
        return null;
    }
}