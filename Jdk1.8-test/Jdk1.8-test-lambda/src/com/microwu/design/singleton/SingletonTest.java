package com.microwu.design.singleton;

/**
 * Description: 单例模式
 *  一个类在生命周期有且只能由一个实例，该类必须自己创建自己的实例，必须给其他对象提供访问点
 *  三个条件：
 *      - 生命周期内有且只能有一个实例
 *      - 自己提供这个实例
 *      - 该实例必须是能全局访问的
 *  细节：最好能实现懒加载，提供启动速度和优化内存
 *
 * https://www.cnblogs.com/kubixuesheng/p/10344533.html
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/26   9:35
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class SingletonTest {

    public static void main(String[] args) {
        Runtime.getRuntime();
    }

}