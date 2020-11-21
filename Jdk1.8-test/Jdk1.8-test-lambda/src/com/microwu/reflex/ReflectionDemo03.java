package com.microwu.reflex;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Description: 先放放
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/23   11:03
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ReflectionDemo03 {

    public static class Person {

    }

    public static abstract class Supper<T, E> {

    }

    public static class Sub extends Supper<String, List<Person>> {

    }

    /**
     * 泛型的简介
     * 泛型的设计是为了应用在 Java 的类型系统，提供 用类型或方法操作各种类型对象从而提供编译器的类型安全功能。
     * 泛型的一个最大优点就是：提供编译期的类型安全。举个简单的例子，在引入泛型之前，ArrayList 内部只维护了一个
     * Object 数组引用，存在两个问题：
     * 1. 从数组列表获取一个元素的时候必须进行类型的强转
     * 2. 想数组列表中添加任何类型的对象，导致无法得知数组列表中存放了什么类型的元素
     * 引入泛型之后，我们可以通过类型参数明确定义：
     * JDK 1.7 以后构造函数可以省略类型，编译器可以推导出实际类型
     * Java 中泛型的事实：
     * 1. Java 虚拟机中不存在泛型，只有普通的类和方法，但是字节码中存放着泛型相关的信息
     * 2. 所有的类型参数都是用它们的限定类型替换
     * 3. 桥方法（Bridge Method）由编译器合成，用于保持多态（Java 虚拟机利用方法的参数类型，方法名称，方法返回值确定一个方法）
     * 4. 为了保持类型的安全性，必要时需要进行类型的强制转换
     * <p>
     * 理解类型擦除
     * 类型擦除（泛型擦除）的具体表现是：无论何时定义一个泛型类型，都自动提供一个相应的原始类型（Raw Type，这里的
     * 原始类型并不是指int 等基本数据类型），原始类型的类名称就是带有泛型参数的类删除泛型参数后的类型名称，而原始
     * 类型会擦除类型变量，并把它们替换为限定类型（如果没有指定限定类型，则擦除为 Object）
     * <p>
     * T -> Object
     * T extend Comparable -> Comparable
     * <p>
     * 为什么需要擦除类型
     * 在 JDK1.5 之前，所有的类型包括基本数据类型（int）、包装类型、自定义的类型都可以使用类文件
     * （.class）字节码对应的 java.lang.Class 描述，也就是 java.lang.Class 类的一个具体实例对象
     * 就可以代表任意一个指定类型的原始类型。这里把泛型出现之前的所有类型暂时称为“历史原始类型”
     * <p>
     * 在 JDK1.5 之后，数据类型得到了扩充：参数化类型，类型变量类型，限定符类型，泛型数组类型。
     * 历史原始类型和新扩充的泛型类型都应该统一成各自的字节码文件类型对象，但是由于代价太大，所以
     * Java 没有在 Java 虚拟机层面引入泛型。
     * <p>
     * Java 为了使用泛型，于是使用了类型擦除的机制引入了“泛型的使用”，并没有真正意义上引入和实现泛型。
     * Java 中的泛型实现的是编译期的类型安全，也就是泛型的类型安全检查是在编译期有编译器 javac 实现的，
     * 这样就能够确保数据基于类型上的安全并且避免了强制类型转换的麻烦（实际上，强制类型转换是由编译器完成的）。
     * 一旦编译完成，所有的泛型类型会被擦除，如果没有指定上线，就会擦除为 Object，否则擦除为上限类型
     * <p>
     * 既然 Java 虚拟机中不存在泛型，那么为什么可以从 JDK 的一些类库中获取泛型信息？因为类文件或者字节码文件
     * 本省存储了泛型的信息
     * <p>
     * Type 体系
     * Class：不解释了
     * ParameterizedType：参数化类型，只要带参数化（泛型）标签 <ClassName> 的参数或者属性，例如：Set<String>
     *
     * JDK 中关于泛型的 API
     *  Class 中的方法：
     *      Type[] getGenericInterfaces() - 返回类实例的接口的泛型类型
     *      Type[] getGenericSuperclass() - 返回类实例的父类的泛型类型
     *  Constructor：
     *      Type[] getGenericParameterTypes - 返回构造器的方法参数的泛型类型
     *  Method：
     *      Type[] getGenericParameterTypes - 返回方法参数的泛型类型
     *      Type[] getGenericReturnType - 返回方法值的泛型类型
     *  Field:
     *      Type getGenericType - 返回属性的泛型类型
     *
     * @param
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/6/23  11:12
     */
    public static void test() throws NoSuchFieldException {
        Class<Sub> subClass = Sub.class;
        Type genericSuperclass = subClass.getGenericSuperclass();
        System.out.println(genericSuperclass);
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            // 获取父类泛型类型数组
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Stream.of(actualTypeArguments).forEach(type ->
                    System.out.println(type + " is  ParameterizedType -> " + (type instanceof ParameterizedType)));

        }
        Field field = subClass.getDeclaredField("clazz");
        Type genericType = field.getGenericType();
        System.out.println(genericType + " is ParameterizedType -> " + (genericType instanceof ParameterizedType));
    }

    /**
     * Type 体系
     *  JDK 1.5 扩充了四种泛型类型：参数化类型（ParameterizedType）、类型变量类型（TypeVariable）、限定符类型（WildcardType）、泛型数组类型（GenericArrayType）
     *
     *  ParameterizedType 参数化类型，实际上只要带参数化泛型标签 <ClassName> 的参数或者属性，都属于 ParameterizedType，如 List<String> list;
     *      |- getActualTypeArguments - 返回这个 ParameterizedType 的实际类型 Type 数组，Type 数组里面可能是 Class，ParameterizedType 或者其他，这个方法仅仅脱去最外层的 <>
     *      |- getRawType - 返回当前这个 ParameterizedType 的原始类型，List<String> -> List.class
     *      |- getOwnerType - 获取原始类型所属的类型，
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/10/27  10:17
     *
     * @param
     * @return  void
     */
    public static void test02() {
        List<String> list = new ArrayList<>();
        list.getClass().getGenericInterfaces();
        Type type = list.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Stream.of(actualTypeArguments).forEach(actualTypeArgument -> {
                System.out.println(actualTypeArgument);
                if (actualTypeArgument instanceof Class) {
                    Class c = (Class) actualTypeArgument;
                    System.out.println("Class：" + c.getName());
                }
            });

            System.out.println("TypeName：" + parameterizedType.getTypeName());
            System.out.println("RawType：" + parameterizedType.getRawType());
            System.out.println("OwnerType：" + parameterizedType.getOwnerType());
        }
    }

    public static void main(String[] args) throws NoSuchFieldException {
//        test();
        test02();
    }
}