package com.microwu.design.observer.c;

/**
 * Description: 显示器
 *
 * 具体的观察者们
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/7/27   15:00
 * Update History:
 * Author        Time            Content
 */
public class CurrentConditionsDisplay implements DisplayElement, Observer {

    private float temperature;
    private float humidity;

    /**
     * 非必须，必要的时候，可以聚合主题接口
     */
    private Subject weatherData;

    // 实现向上转型，面向接口编程，解耦合
    public CurrentConditionsDisplay(Subject weatherData) {
        this.weatherData = weatherData;
        // 将自己注册到主题中
        weatherData.registerObserver(this);
    }

    @Override
    public void display() {
        System.out.println("Current conditions: " + temperature
                + "degrees and " + humidity + "is % humidity");
    }

    @Override
    public void update(float temp, float humidity, float pressure) {
        this.temperature = temp;
        this.humidity = humidity;
        display();
    }
}
