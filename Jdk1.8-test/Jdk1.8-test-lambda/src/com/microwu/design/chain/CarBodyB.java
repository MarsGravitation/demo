package com.microwu.design.chain;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/7/28   11:08
 * Update History:
 * Author        Time            Content
 */
public class CarBodyB extends CarControllerB {
    @Override
    public void controlCar() {
        System.out.println("组装汽车的身体");

        if (getSuccessor() != null) {
            getSuccessor().controlCar();
        }
    }
}
