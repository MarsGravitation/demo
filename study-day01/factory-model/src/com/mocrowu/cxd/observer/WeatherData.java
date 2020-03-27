package com.mocrowu.cxd.observer;

import java.util.ArrayList;
import java.util.List;

public class WeatherData implements Subject {
    private List<Observer> list;
    private float tempterature;
    private float pressure;
    private float humidity;

    public WeatherData() {
        this.list = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer observer) {
        list.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        if(list.size() > 0){
            list.remove(observer);
        }
    }

    @Override
    public void notifyObserver() {
        for(int i = 0; i < list.size(); i++){
            Observer observer = list.get(i);
            observer.update(tempterature, humidity, pressure);
        }
    }

    /**
     * 气象站观测到新的数据，通知观察者
     */
    public void measurementChanged(){
        notifyObserver();
    }

    public void setMeasurement(float tempterature, float humidity, float pressure){
        this.tempterature = tempterature;
        this.humidity = humidity;
        this.pressure = pressure;
        measurementChanged();
    }
}
