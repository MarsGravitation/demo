package com.microwu.design.observer.c;

import java.util.Observable;
import java.util.Observer;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/7/27   15:16
 * Update History:
 * Author        Time            Content
 */
public class CurrentConditionsDisplay2 implements Observer, DisplayElement {

    private Observable observable;
    private float temperature;
    private float humidity;

    public CurrentConditionsDisplay2(Observable observable) {
        this.observable = observable;
        this.observable.addObserver(this);
    }

    @Override
    public void display() {
        System.out.println("Current conditions: " + temperature
                + "F degrees and " + humidity + "% humidity");
    }

    /**
     * 实现拉模型，arg 是空
     * 额外多了 Observable o 参数，让观察者知道：到底是哪个主题通知的
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2022/7/27 15:18
     *
     * @param: o - [java.util.Observable]
     * @param: arg - [java.lang.Object]
     * @return: void
     */
    @Override
    public void update(Observable o, Object arg) {
        // 非常灵活的设计，可以指定观察者只响应特定主题的通知，而不是默认全部通知
        if (o instanceof WeatherData2) {
            WeatherData2 weatherData2 = (WeatherData2) o;
            // 拉数据，让观察者具有了很大的灵活性 --- 自己决定需要什么数据
            this.temperature = weatherData2.getTemperature();
            this.humidity = weatherData2.getHumidity();
            display();
        }
    }
}
