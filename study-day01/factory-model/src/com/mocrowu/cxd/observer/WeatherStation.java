package com.mocrowu.cxd.observer;

/**
 * 观察者模式：
 *      被观察者要保存观察者的引用，当触发某一条件时，要通知观察者
 */
public class WeatherStation {
    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();
        CurrentConditionDisplay currentConditionDisplay = new CurrentConditionDisplay(weatherData);

        weatherData.setMeasurement(80, 65, 30.4f);
    }
}
