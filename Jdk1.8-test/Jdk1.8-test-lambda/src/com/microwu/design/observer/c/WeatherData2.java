package com.microwu.design.observer.c;

import java.util.Observable;

/**
 * Description: 拉模型
 *
 * class Observable: 主题
 * interface Observer: 观察者
 *
 * 继承 Observable，继承一些增加、删除、通知观察者的方法
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/7/27   15:11
 * Update History:
 * Author        Time            Content
 */
public class WeatherData2 extends Observable {
    private float temperature;
    private float humidity;
    private float pressure;

    public WeatherData2() {
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getPressure() {
        return pressure;
    }

    private void measurementsChanged() {
        // 线程安全
        // 拉模型实现，只是设置一个状态，如果状态位变了，就说明数据变更
        setChanged();
        // 线程安全，只有当状态位为 true，通知才有效，之后观察着门会主动拉取数据
        notifyObservers();
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;

        measurementsChanged();
    }
}
