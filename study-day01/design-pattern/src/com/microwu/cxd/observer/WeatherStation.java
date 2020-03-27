package com.microwu.cxd.observer;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/24   13:56
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class WeatherStation {
    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();

        new CurrentConditionsDisplay(weatherData);
        weatherData.setMeasurements(80, 65, 34);
    }
}