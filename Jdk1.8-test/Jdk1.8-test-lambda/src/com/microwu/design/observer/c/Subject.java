package com.microwu.design.observer.c;

/**
 * Description: 主题接口
 *
 * 主要提供增加观察者，通知观察者
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/7/27   14:49
 * Update History:
 * Author        Time            Content
 */
public interface Subject {
    void registerObserver(Observer o);
    void remoteObserver(Observer o);
    void notifyObservers();
}
