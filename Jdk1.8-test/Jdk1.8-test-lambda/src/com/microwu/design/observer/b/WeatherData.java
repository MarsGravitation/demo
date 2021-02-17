package com.microwu.design.observer.b;

/**
 * Description:
 *  1. Weather 具有 getter 方法，可以从气象站取得测量值
 *  2. 当 WeatherData 从气象站获得了最新的测量数据，measurementsChanged 方法必须被调用
 *  3. 需要对接的 RD 实现天气预报的显示功能，一旦 WeatherData 获取了新的测量数据，这些数据必须也同步更新到页面
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/26   14:14
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class WeatherData {

    public int getTemperature() {
        return 0;
    }

    public int getHumidity() {
        return 0;
    }

    public int getPressure() {
        return 0;
    }

    /**
     * 问题：
     * 改进：
     *  1. 三个 update 可以抽象出一个接口
     *  2. 显示器的实现对象们，明显是经常需要改变的部分，应该拆分变化的部分，并且独立抽取出来，做封装
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/11/26  14:21
     *
     * @param
     * @return  void
     */
    public void measurementsChanged() {
        int temperature = getTemperature();
        int humidity = getHumidity();
        int pressure = getPressure();

        // 三种显示器的实现类的对象
//        currentConditionsDisplay.update(temp, humidity, pressure);
//        statisticsDisplay.update(temp, humidity, pressure);
//        forecastDisplay.update(temp, humidity, pressure);
    }

}