package com.mocrowu.cxd.observer;

/**
 * 观察者接口
 */
public interface Observer {
    public void update(float temp, float humidity, float pressure);
}
