package com.microwu.generic;

import com.microwu.concurrent.limit.AtomicRateLimit;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 泛型测试类 - https://juejin.im/post/5e86a58d6fb9a03c8b4bf701
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:                   2020/4/18   10:45
 * Copyright:              北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * <p>
 * Author        Time            Content
 */
public class GenericTest {

    /**
     * 1. 泛型用来解决什么问题？
     *      > 避免运行时出错，编译时就告诉你
     *      > 方便使用，省略强制类型转换的代码
     *
     * 2. 泛型为什么不可以是静态的？
     *      泛型主要是用来初始化每个实例的。注意是每个实例，他是动态确定的，
     *      取决于你当时使用的时候 穿什么参数，比如 List<String>, List<Integer>
     *      但是对于静态变量，是全局唯一的。 所以泛型是不能用static 修饰的。
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/18  10:48
     *
     * @param   	
     * @return  void
     */
    public static void test() {

    }

    /**
     * 泛型限制在list 中的迷惑性
     *
     * 总结：
     *  1. Fruit fruit = new Apple();
     *      左边想要一个水果，右边给一个苹果，编译通过
     *
     *     List<Fruit> fruits = new ArrayList<Fruit>();
     *     fruits.add(new Apple());
     *     fruits.add(new Banana());
     *          左边要一个水果的list，右边给了一个水果list
     *          那添加 苹果，香蕉没问题
     *
     *  2. List<Fruit> fruits = new ArrayList<Apple>();
     *      左边要一个水果list，右边给一个苹果list，报错
     *
     *  3. List<? extends Fruit> fruits = new ArrayList<Apple>();
     *      左边要一个 水果类型的list，且只能是一种水果，右边给一个苹果list，可以
     *      fruits.add(new Apple()); - 报错，因为左边是要一个水果类型，但是你给我
     *      一个确定好的水果，编译失败
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/18  10:53
     *
     * @param
     * @return  void
     */
    public static void test02() {
        // 正常编译
//        List<Fruit> fruits = new ArrayList<>();
        // 1。 编译一错误

        // 声明了一个水果集合，但实际是苹果的集合，
        // 但是向其中添加香蕉，这样逻辑不通

        // 所以对于 list的泛型，他的类型是动态决定的，没办法在编译器确定的
        // 你需要保证 = 两边泛型是绝对一致
//        List<Fruit> fruitList = new ArrayList<Apple>();
//        fruitList.add(new Banana());

        // 2. 编译二错误

        // 左边声明 这个 list只要是fruit的子类型就可以
        // 但是在编译期 没办法确定具体类型
        // 如果通过编译， 运行时不确定是香蕉list 还是 苹果list
//        List<? extends Fruit> fruits = new ArrayList<>();
//        fruits.add(new Banana());
//        fruits.add(new Apple());

        // 3. 编译三错误
        List<? extends Fruit> fruits = new ArrayList<Banana>(); // 编译通过
        // 编译期不确定 fruits到底是香蕉还是苹果，所以不能添加
//        fruits.add(new Banana());
//        fruits.add(new Apple());

    }

    /**
     * ? extends 好像有点蠢
     *
     *  int getAllPrice(List<Fruit> fruits);
     *  函数想表达的意思是 计算任意一种水果集合的总价格
     *  getAllPrice(List <Apple>) -> 报错
     *
     *  int getAllPrice(List <? extends Fruit> fruits)
     *  getAllPrice(List <Apple>) -> 通过
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/18  11:18
     *
     * @param
     * @return  void
     */
    public static void test03() {

    }

    /**
     * ? super 怎么理解
     *
     * List<? super Apple> apples = new List<Fruit>()
     * 我想要一个可以装苹果的list， 你给我一个水果list， 可以
     *
     *
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/18  11:26
     *
     * @param
     * @return  void
     */
    public static void test04() {

    }

    public static void main(String[] args) {

    }
}