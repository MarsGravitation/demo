package com.microwu.design.chain;

/**
 * Description:
 *
 * 类图：
 *  1. 抽象处理者角色（Handler）
 *      定义出一个处理请求的抽象接口。定义对下家的引用，同时提供 get/set 方法，
 *      抽象类或者接口实现
 *  2. 具体矗立着角色（ConcreteHandler）
 *      继承 Handler，可以选择处理或者忽略
 *  3. client
 *      组装责任链
 *
 *
 * https://mp.weixin.qq.com/s?__biz=MzU1NjY0NzI3OQ==&mid=2247484798&idx=2&sn=c035ea50fc6c59df62170314cc1f9df5&chksm=fbc0937eccb71a68bbd0dbc138078b903bf34b06a4efed9f4de5f6dfa6c442292349827ef038&scene=21#wechat_redirect
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/7/28   10:54
 * Update History:
 * Author        Time            Content
 */
public class MainClass {

    public static void main(String[] args) {
        // 进行组装
        CarHead carHead = new CarHead();
        CarBody carBody = new CarBody();
        CarTail carTail = new CarTail();

        // 手动的实现组装过程
        carHead.controlCar();
        carBody.controlCar();
        carTail.controlCar();

        System.out.println("===========");
        Client client = new Client();
        client.execute();
    }
}
