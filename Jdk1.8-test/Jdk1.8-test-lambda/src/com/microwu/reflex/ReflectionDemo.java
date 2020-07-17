package com.microwu.reflex;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.stream.Stream;

/**
 * Description:
 * 1. 什么是反射？
 * 反射是一种可以在运行时检查和动态调用类、构造、方法、属性等等的编程语言的能力，
 * 甚至可以不需要在编译期感知类的名称、方法的名称等等。官方教程指出反射是由应用程序使用，
 * 用于检查或修改在 Java 虚拟机中运行的应用程序的运行时行为，这是一个相对高级的功能，需要
 * 由掌握 Java 语言基础知识的开发者使用
 * <p>
 * 缺点：
 * 1. 性能开销：由于反射涉及动态解析的类型，因此无法执行某些 Java 虚拟机优化
 * 2. 安全限制：反射需要运行时权限，不能在安全管理器 security manager 下进行反射操作
 * 3. 代码可移植性
 * <p>
 * 反射核心类的体系
 * Class、Constructor、Method、Field、Parameter
 * <p>
 * 1. Class、Constructor、Method、Field、Parameter 共有的父接口是 AnnotatedElement
 * 2. Constructor、Method、Field 共有的父类时 AnnotatedElement、AccessibleObject 和 Member
 * 3. Constructor、Method 共有的父类是 AnnotatedElement、AccessibleObject、Member、GenericDeclaration 和 Executable
 * <p>
 * 这里先说一个规律，在 Class 中，getXXX() 方法和 getDeclaredXXX() 方法有所区别。注解类型 Annotation 的操作方法除外，
 * 因为基于注解的修饰符必定是 public 的
 * 1. getDeclaredMethods：返回类或接口声明的所有方法，包括公共、保护、默认访问和私有方法，当不包括继承的方法
 * 2. getMethod：返回某个类的所有公用 public 方法包括其继承类的公用方法，当然也包括它所实现接口的方法
 * <p>
 * 如果向获取一个类的所有修饰符的方法，包括所有父类中的方法，建议递归调用 getDeclaredMethods，可以参考 Spring 的工具类。
 *
 * @Inherited 阐述了某个被标注的 Annotation 类型可以被继承
 * <p>
 * Type 接口：是 Java 中所有类型的共同父类，包括原始类型，泛型类型、数组类型、类型变量和基本类型
 * AnnotatedElement 接口：它定义的方法主要是和注解操作相关
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/22   10:54
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ReflectionDemo {

    private String[] strings = new String[]{};

    @SupperAnnotation
    public static class Supper {

        private int a = 1;

        public void setA(int a) {
            this.a = a;
        }

        public int getA() {
            return this.a;
        }

        public void hello(String world) {
            System.out.println("hello " + world);
        }

    }

    @SubAnnotation
    private static class Sub extends Supper {

    }

    @Retention(RetentionPolicy.RUNTIME)
    // 如果注释掉这个注解，那么子类永远无法获取父类中的注解
    @Inherited
    @Documented
    @Target(ElementType.TYPE)
    private @interface SupperAnnotation {
        String value() default "SupperAnnotation";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Target(ElementType.TYPE)
    private @interface SubAnnotation {
        String value() default "SubAnnotation";
    }

    /**
     * 内部类是私有的，私有内部类
     * 拥有内部类的类编译后 内外部类两者没有关系，那么私有内部类编译后默认是没有对外构造器的
     * 但是我们有知道，外部类是可以引用内部类的，那么编译后，优势两个毫无关系的类，一个类没对外构造器
     * 但另一个类确实是对这个类的实例对象权限，这种情况下，编译器就会生成一个合成类，外部类$1，
     * 并不太清楚原理
     */
    private static class PriInner {

    }

    /**
     * 为什么 内部类可以访问内部类的私有属性？
     *  1. 在内部类构造的时候，会将外部类的引用传递进来，并作为内部类的一个属性，
     *      所以内部类会持有外部类的一个引用(但我认为不是这个原因)
     *  2. 实际上是编译器生成了一个静态方法，内部类可以调用这个方法获取私有属性的值
     */
    public class PubInner {
        private int a;

        private void print() {
            System.out.println(strings);
        }
    }

    static void checkSynthetic(String name) {
        try {
            System.out.println(name + " : " + Class.forName(name).isSynthetic());
        } catch (ClassNotFoundException e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * AnnotatedElement 接口
     *
     * @param
     * @return void
     * @author chengxudong               chengxudong@microwu.com
     * @date 2020/6/22  14:17
     */
    public static void test() {
        Class<Sub> clazz = Sub.class;
        System.out.println("==========getAnnotations===========");
        Annotation[] annotations = clazz.getAnnotations();
        Stream.of(annotations).forEach(System.out::println);
        System.out.println("=========getDeclaredAnnotation -> SupperAnnotation ==========");
        SupperAnnotation declaredAnnotation = clazz.getDeclaredAnnotation(SupperAnnotation.class);
        System.out.println(declaredAnnotation);
        System.out.println("============ getAnnotation -> SupperAnnotation =============");
        SupperAnnotation annotation = clazz.getAnnotation(SupperAnnotation.class);
        System.out.println(annotation);
        System.out.println("============== getDeclaredAnnotation -> SubAnnotation ===============");
        SubAnnotation subAnnotation = clazz.getDeclaredAnnotation(SubAnnotation.class);
        System.out.println(subAnnotation);
        System.out.println("=============getDeclaredAnnotationsByType -> SubAnnotation===============");
        SubAnnotation[] declaredAnnotationsByType = clazz.getDeclaredAnnotationsByType(SubAnnotation.class);
        Stream.of(declaredAnnotationsByType).forEach(System.out::println);
        System.out.println("============getDeclaredAnnotationsByType -> SupperAnnotation ============");
        SupperAnnotation[] supperAnnotations = clazz.getDeclaredAnnotationsByType(SupperAnnotation.class);
        Stream.of(supperAnnotations).forEach(System.out::println);
        System.out.println("=========getAnnotationsByType -> SupperAnnotation==========");
        SupperAnnotation[] annotationsByType = clazz.getAnnotationsByType(SupperAnnotation.class);
        Stream.of(annotationsByType).forEach(System.out::println);

    }

    /**
     * Member 接口
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/22  14:18
     *
     * @param
     * @return  void
     */
    public static void test02() {
        new ReflectionDemo().new PubInner();
        checkSynthetic("com.microwu.reflex.ReflectionDemo");
    }

    /**
     * AccessibleObject 是一个普通的 Java 类，实现了 AnnotationElement 接口，
     *
     * setAccessible - 设置实例是否可以访问，true 可以抑制修饰符，直接进行访问
     */
    public void test03() {

    }

    /**
     * GenericDeclaration 接口，继承 AnnotatedElement
     * 后面再展开吧
     */
    public void test04() {

    }

    /**
     * Executable 类，抽象类，继承 AccessibleObject，实现了 Member 和 GenericDeclaration 接口。
     * Executable 的实现类是 Method 和 Constructor，它的主要功能是 Method 和 Constructor 抽取出
     * 两者可以公用的一些方法，例如注解的操作，参数的操作等等
     *
     * Modifier
     */
    public void test05() {

    }

    /**
     * Class，提供了 类型判断，类型实例化、获取方法列表、字段列表、父类泛型类型
     *
     * getName - 用于获取类在 Java 虚拟机中的类名表示
     * getCanonicalName - 用于获取全类名，包括包路径，包路径以点号分割
     * getSimpleName - 不包括路径
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/22  17:09
     *
     * @param
     * @return  void
     */
    public static void test06() {
        Supper supper = new Supper();
        Class<? extends Supper> clazz = supper.getClass();
        System.out.println("name: " + clazz.getName());
        System.out.println("canonicalName: " + clazz.getCanonicalName());
        System.out.println("simpleName: " + clazz.getSimpleName());
        System.out.println("==================================");
        String[][] strings = new String[1][1];
        System.out.println("name: " + strings.getClass().getName());
        System.out.println("canonicalName: " + strings.getClass().getCanonicalName());
        System.out.println("simpleName: " + strings.getClass().getSimpleName());
    }

    /**
     * Class.forName 只能使用在修饰符为 public 的类上，否则会抛出 IllegalAccessException
     * 获取 Class 实例的方式：
     *  1. 类名.class。类字面常量使得创建 Class 对象的引用时不会自动地初始化该对象，而是按照之前提到
     *  的加载、链接、初始化三个步骤，这三个步骤是个懒加载的过程，不使用的时候就不加载
     *  2. Class.forName
     *  3. 实例的 getClass 方法。这个方法是 Object 类的方法，所有对象都有此方法
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/22  17:38
     *
     * @param   	
     * @return  void
     */
    public static void test07() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> clazz = Class.forName("com.microwu.reflex.ReflectionDemo$Supper");
        Supper supper = (Supper) clazz.newInstance();
        supper.hello("world");
    }

    /**
     * Constructor 用于描述一个类的构造函数。
     * 它除了能获取到构造的注解信息、参数的注解信息、参数信息
     * 还可以抑制修饰符进行实例化
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/22  17:46
     *
     * @param
     * @return  void
     */
    public static void test08() throws NoSuchMethodException, IllegalAccessException, InstantiationException {
        Class<Supper> clazz = Supper.class;
        Constructor<Supper> declaredConstructor = clazz.getDeclaredConstructor();
        declaredConstructor.setAccessible(Boolean.TRUE);
        Supper supper = clazz.newInstance();
        supper.hello("world");

    }

    /**
     * Method 用于描述一个类的方法。获取方法的注解信息，方法参数，返回值的注解信息和其他信息
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/22  17:53
     *
     * @param
     * @return  void
     */
    public static void test09() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<Supper> clazz = Supper.class;
        Supper supper = clazz.newInstance();
        Method[] methods = clazz.getDeclaredMethods();
        Stream.of(methods).forEach(System.out::println);
        Method method = clazz.getDeclaredMethod("hello", String.class);
        method.setAccessible(true);
        method.invoke(supper, "world");
    }

    /**
     * Field 类用来描述一个类里面的属性或者叫成员变量
     * 可以获取到属性的注解信息，泛型信息，获取和设置属性的值等
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/23  9:11
     *
     * @param
     * @return  void
     */
    public static void test10() throws IllegalAccessException, InstantiationException, NoSuchMethodException, NoSuchFieldException {
        Class<Supper> clazz = Supper.class;
        Supper supper = clazz.newInstance();
        Field a = clazz.getDeclaredField("a");
        a.setAccessible(true);
        a.setInt(supper, 2);
        System.out.println(a.get(supper));

    }

    /**
     * Parameter 类，用于描述 Method 或者 Constructor 的参数，主要是用于获取参数的名称。
     * 因为在 Java 中没有形式参数的概念，也就是参数都是没有名称的。 jdk 1.8 新增了 Parameter
     * 来填补这个问题，获取 Parameter 的方法是 Method 或者 Constructor 的父类 Executable
     * 的 getParameters 方法。一般而言， Parameter 用于获取参数名称的后备方案，因为 jdk 1.8
     * 之前没有这个类，及时使用了 1.8，但是javac 编译器没有加上 -parameters 参数的话，获取的参数
     * 名称时 arg0, 类似的没有意义的参数名称，而加上了该参数，会在生成的 .class 文件中额外存储参数
     * 的元信息，导致 .class 文件的大小增加。一般框架中使用其他的方法解析方法或者构造器的参数名称
     * Spring - LocalVariableTableParameterNameDiscoverer，是使用 ASM 去解析和读取类文件字节码
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/6/23  9:18
     *
     * @param
     * @return  void
     */
    public static void test11() throws IllegalAccessException, InstantiationException, NoSuchMethodException {
        Class<Supper> supperClass = Supper.class;
        Method hello = supperClass.getDeclaredMethod("hello", String.class);
        Parameter[] parameters = hello.getParameters();
        Stream.of(parameters).forEach(parameter -> System.out.println(parameter.getName()));
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
//        test();
//        test02();
//        test06();
//        test07();
//        test09();
//        test10();
        test11();
    }
}