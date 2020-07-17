package com.microwu.reflex;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/23   9:31
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ReflectionDemo02 {

    enum Color {
        RED, BLACK, BLUE
    }
    
    /**
     * 数组类型
     *
     * 数组是一种包含固定数量的相同类型组件（Component）的引用类型对象，
     * 也就是说数组的长度是不可变的，它的每个元素都是相同类型的。创建数组实例
     * 需要定义数组的长度和组件的类型。数组是由 Java 虚拟机实现的（这一点很重要，
     * 这也是为什么 JDK 类库中没有数组对应的类型的原因，array 也不是Java 中的保留关键字，
     * 操作数组的底层方法都是 native 方法），数组类型只有继承 Object 方法，数组的 length
     * 方法实际上并不属于数组类型的一部分，数组的 length 方法其实最终调用的是 Array#getLength，
     * 基于反射操作数组的核心类，属于 native 方法
     *
     * 使用非反射方式创建数组实例：
     *  fully_qualified_class_name[] variable_name = {}
     *  fully_qualified_class_name[] variable_name = new fully_qualified_class_name[${fix_length}]
     *
     *  int[] arr = new int[10];
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/23  9:31
     *
     * @param   	
     * @return  void
     */
    public static void test() {
//        Class<?> c = Class.forName(cName);
//        Object o = Array.newInstance(c, n);
//        for (int i = 0; i < 10; i++) {
//            String v = cVals[i];
//            Constructor<?> constructor = c.getConstructor(String.class);
//            Object val = constructor.newInstance(v);
//            Array.set(o, i, val);
//        }
//
//        Object[] oo = (Object[]) o;

    }

    /**
     * 目标数组
     */
    private static String R = "java.math.BigInteger[] bi = {123,234,345}";
    private static final String[] S = new String[]{"123", "234", "345"};

    /**
     * 细议数组类型
     *  创建特定元素类型的数组
     *      因为 Java 泛型擦除的问题，实际上我们使用 Array#newInstance 方法只能得到一个 Object 类型的结果实例，
     *      其实这个结果实例的类型就是 ComponentType[]，这里只是反悔了它的父类（Object） 类型实例，我们可以直接强转
     *
     *      String[] strArray = (String[]) Array.newInstance(String.class, 3)
     *
     *  获取数组类型
     *      非反射方式下 - Class stringArrayClass = String[].class
     *      反射方式 - Class.forName("[Ljava.lang.String;")
     *      但是有个限制，类名必须是 JVM 可以识别的签名形式，就是 [L${ComponentType}，无法获取原始类型，如int
     *
     *  获取数组元素（组件）类型
     *      只能通过数组类型实例去调用 Class#getCommonentType
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/23  9:58
     *
     * @param
     * @return  void
     */
    public static void test02() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<BigInteger> componentType = BigInteger.class;
        Object arrayObject = Array.newInstance(componentType, 3);
        for (int i = 0; i < S.length; i++) {
            String each = S[i];
            Constructor<BigInteger> constructor = componentType.getConstructor(String.class);
            BigInteger value = constructor.newInstance(each);
            Array.set(arrayObject, i, value);
        }

        Object[] result = (Object[]) arrayObject;
        System.out.println(Arrays.toString(result));

        Class<?> aClass = arrayObject.getClass();
        System.out.println(aClass.getComponentType());

    }

    /**
     * 枚举类型
     *  枚举是一种语言结构（Language Construct），用于定义可以使用一组固定的名值对表示的类型安全的枚举。
     *  所有的枚举都继承自 java.lang.Enum。枚举可以包含一个活这多个枚举常量，这些枚举常量都是该枚举的实例。
     *
     * 枚举类的特点
     *  1. 枚举类会变成一个普通的 Java 类，这个 Java 类会继承 java.lang.Enum，并且把自身类型作为泛型参数类型，
     *  构造函数中必定包含name, order 参数，因为父类的构造要求传入这两个参数
     *  2. 所有的枚举成员属性都变成 static final 修饰，属性的名称和原来枚举的名字一致，实例在静态代码块中创建
     *  3. 新增了一个 static final 的实例数组，名称为 $VALUES，此数组在静态代码块中创建，基于此数组还新增了一个静态
     *  方法 values()，此方法就是直接返回数组的克隆
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/23  10:45
     *
     * @param
     * @return  void
     */
    public static void test03() {
        Class<Color> colorClass = Color.class;
        System.out.println(colorClass.isEnum());
        System.out.println(Arrays.toString(colorClass.getEnumConstants()));
    }
    
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//        test02();
        test03();
    }
}