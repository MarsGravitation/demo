package com.microwu.design.observer.c;

/**
 * Description:
 *
 * 观察者模式所涉及的角色：
 *  1. 抽象主题角色
 *  抽象主题角色把所有对观察者的引用（注册的观察者，订阅者）保存在一个聚集
 *  （ArrayList） 里，每个主题都可以有任何数量的观察者。抽象者提供一个接口，
 *  可以增加和删除观察者对象，抽象主题角色又叫做抽象被观察者角色
 *  2. 具体主题角色
 *  将有关状态存入具体观察者对象。在具体主题的内部状态改变时，给所有登记过的观察者
 *  发出通知。具体主题角色又叫做具体被观察者角色。
 *  3. 抽象观察者角色
 *  为所有的具体观察者定义一个接口，实现 update 行为，在得到主题的通知时，
 *  update 被调用，从而能够更新自己
 *  4. 具体观察者角色
 *
 *
 * https://mp.weixin.qq.com/s/ZFyoRxHJFK6ODTZkW9fEuA
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/7/27   14:42
 * Update History:
 * Author        Time            Content
 */
public class ObserverState {

    public static void main(String[] args) {
        // 主题
        WeatherData weatherData = new WeatherData();
        // 观察者注册到主题
        CurrentConditionsDisplay conditionsDisplay = new CurrentConditionsDisplay(weatherData);
        // 更新数据
        weatherData.setMeasurements(80, 65, 30.4f);
    }
}
