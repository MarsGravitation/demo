package com.microwu.cxd.design.rookie.factory;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/23   14:03
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ShapeFactory {

    public static Shape getShape(String shapeType) {
        if(shapeType == null) {
            return null;
        }
        if(shapeType.equalsIgnoreCase("CIRCLE")) {
            return new Circle();
        }
        if(shapeType.equalsIgnoreCase("RECTANGLE")) {
            return new Rectangle();
        }
        if(shapeType.equalsIgnoreCase("SQUARE")) {
            return new Square();
        }
        return null;
    }
}