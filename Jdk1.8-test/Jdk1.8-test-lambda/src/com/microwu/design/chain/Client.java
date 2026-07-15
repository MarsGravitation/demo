package com.microwu.design.chain;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/7/28   11:10
 * Update History:
 * Author        Time            Content
 */
public class Client {

    /**
     * 在环境类（客户端）里按照业务需要，动态的组装各个责任类为一条链条
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/7/28 11:11
     *
     * @param:
     * @return: void
     */
    public void execute() {
        CarHeadB headB = new CarHeadB();
        CarBodyB bodyB = new CarBodyB();
        CarTailB tailB = new CarTailB();

        // 灵活的组装生产线的顺序
        headB.setSuccessor(bodyB);
        bodyB.setSuccessor(tailB);

        // 自动的开启生产线，调用链条头部
        headB.controlCar();
    }

}
