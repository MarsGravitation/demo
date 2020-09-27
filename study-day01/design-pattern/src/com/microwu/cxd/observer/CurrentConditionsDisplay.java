package com.microwu.cxd.observer;

/**
 * Description:     观察者
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/24   13:53
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CurrentConditionsDisplay implements  Observer, DisplayElement {
    private float temperature;
    private float humidity;
    private Subject weatherData;

    public CurrentConditionsDisplay(Subject weatherData) {
        this.weatherData = weatherData;
        this.weatherData.registerObserver(this);
    }

    @Override
    public void display() {
        System.out.println(temperature + ", " + humidity);
    }

    @Override
    public void update(float temp, float humidity, float pressure) {
        this.temperature = temp;
        this.humidity = humidity;
        display();

    }
}