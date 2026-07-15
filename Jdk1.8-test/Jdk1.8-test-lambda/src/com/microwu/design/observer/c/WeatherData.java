package com.microwu.design.observer.c;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 具体的主题 --- 气象观测系统
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/7/27   14:52
 * Update History:
 * Author        Time            Content
 */
public class WeatherData implements Subject {

    private List<Observer> observers;
    private float temperature;
    private float humidity;
    private float pressure;

    public WeatherData() {
        observers = new ArrayList<>();
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

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void remoteObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer obs : observers) {
            obs.update(temperature, humidity, pressure);
        }
    }

    private void measurementsChanged() {
        notifyObservers();
    }

    /**
     * 被动接收气象站的数据更新，或者主动抓取
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/7/27 14:57
     *
     * @param: temperature - [float]
     * @param: humidity - [float]
     * @param: pressure - [float]
     * @return: void
     */
    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        // 一旦数据更新了，就立即同步观察者们，这就是所谓的 push --- 推模式
        measurementsChanged();
    }
}
