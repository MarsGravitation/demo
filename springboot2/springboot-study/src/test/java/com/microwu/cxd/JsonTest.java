package com.microwu.cxd;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.config.ConstructorArgumentValues;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/1   14:36
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class JsonTest {

    public static void main(String[] args) {
        ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();

        constructorArgumentValues.addIndexedArgumentValue(0, new ConstructorArgumentValues.ValueHolder(0));
        constructorArgumentValues.addIndexedArgumentValue(1, new ConstructorArgumentValues.ValueHolder(1));

        String string = JSON.toJSONString(constructorArgumentValues);
        System.out.println(string);

        ConstructorArgumentValues constructorArgumentValues1 = JSON.parseObject(string, ConstructorArgumentValues.class);
        System.out.println(constructorArgumentValues1);

    }
}