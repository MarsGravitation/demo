package com.microwu.design.observer.c;

/**
 * Description: 观察者接口
 *
 * 观察者要更新自己
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/7/27   14:50
 * Update History:
 * Author        Time            Content
 */
public interface Observer {
    void update(float temp, float humidity, float pressure);
}
