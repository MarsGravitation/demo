package com.mocrowu.cxd.strategy;

/**
 *  策略模式：
 *      情景：现在有三种排序算法，需要根据不同的情况选择不同的算法，如果使用if...else进行判断比较麻烦
 *  角色：
 *      Strategy：抽象策略类 --- Sort
 *      ConcreteStrategy：具体策略类 --- SelectSort
 *      Context：上下文，保存了策略引用，可以通过设置更改策略
 */
public class Client {
    public static void main(String[] args) {
        ArrayHandler arrayHandler = new ArrayHandler();
        SelectSort selectSort = new SelectSort();
        arrayHandler.setSort(selectSort);
        arrayHandler.sort(new int[]{1, 2});
    }
}
