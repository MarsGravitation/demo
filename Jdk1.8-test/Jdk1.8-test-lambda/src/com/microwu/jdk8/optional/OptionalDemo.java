package com.microwu.jdk8.optional;

import java.util.Optional;

/**
 * Description: Optional 解决空指针异常
 *  Optional 类是一个封转了Optional值的容器对象, Optional值可以为null, 如果值存在,
 *  调用isPresent() 方法返回true, 调用get()方法可以获取值
 *
 *  通过源代码分析, 它并没有实现序列化接口, 因此应避免在类属性中使用, 防止意想不到的问题
 *
 *  1. 正确的使用创建方法, 不确定是否为null时尽量选择ofNullable方法
 *  2. 避免在成员变量上使用
 *  3. 避免直接调用Optional对象的get 和 isPresent 方法
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/10/17   14:44
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class OptionalDemo {
    public static void main(String[] args) {
        // 1. 创建Optional对象的三种方法
        // 1.1 Optional 对象没有值则用empty()方法
//        Optional.empty();
        // 1.2 如果确定Optional 对象的值不为null, 则可以使用of() 方法
//        Optional.of("a");
        // 1.3 如果不确定 Optional对象的值是否为null, 则可用ofNullable(), 当然, 也可以直接给方法传null
        Optional<String> o = Optional.ofNullable("a");
        // isPresent() 查看是否为null
        boolean present = o.isPresent();
        System.out.println(present);
        // 如果直接调用get() 方法, 则会抛出异常
        o.get();
        // 1.4 map 获取Optional中的值
        System.out.println(o.map(Object::toString));
        // 1.5 flatMap 和map类似, 但返回值必须为Optional
        // 1.6 orElse 如果有值就返回, 否则返回一个默认值
        // 和 str != null ? str : "--" 效果类似
        Object o1 = o.orElse("--");
        System.out.println(o1);

    }

}