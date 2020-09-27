package com.microwu.enumx;

/**
 * Description:
 *  1. 枚举的声明格式： {ClassModifier} enum Identifier [Superinterfaces] EnumBody
 *      ClassModifier 是修饰符，Identifier 是枚举名称，枚举类型可以实现接口
 *  2. 不能使用 abstract 或者 final 修饰，否则会产生编译错误
 *  3. 枚举类型的直接超类是 Enum
 *  4. 除了枚举常量定义之外没有其他实例，也就是枚举类型不能实例化
 *  5. 禁用反射操作进行实例化
 *
 *  枚举类型的成员属性是在静态代码块中实例化的
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/24   13:43
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public enum PhoneOsEnum {

    ANDROID(1, "android"),
    IOS(2, "ios");

    private final Integer type;
    private final String typeName;

    PhoneOsEnum(Integer type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    public Integer getType() {
        return this.type;
    }

    public String getTypeName() {
        return this.typeName;
    }

}