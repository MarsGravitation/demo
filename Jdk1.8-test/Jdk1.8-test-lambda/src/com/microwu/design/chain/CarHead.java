package com.microwu.design.chain;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/7/28   10:56
 * Update History:
 * Author        Time            Content
 */
public class CarHead extends CarController {

    /**
     * 具体的组装责任（任务）
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/7/28 10:56
     *
     * @param:
     * @return: void
     */
    @Override
    public void controlCar() {
        System.out.println("组装汽车的头部");
    }
}
