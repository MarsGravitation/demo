package com.microwu.design.chain;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/7/28   11:03
 * Update History:
 * Author        Time            Content
 */
public abstract class CarControllerB {

    /**
     * 各个责任类要持有的下一个对象引用
     */
    protected CarControllerB successor;

    public CarControllerB getSuccessor() {
        return successor;
    }

    public void setSuccessor(CarControllerB successor) {
        this.successor = successor;
    }

    /**
     * 控制组装车的组装过程
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/7/28 11:05
     *
     * @param:
     * @return: void
     */
    public abstract void controlCar();
}
