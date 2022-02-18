package com.microwu.cxd.bean.pojo;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2021/1/13   9:57
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Car {

    private int maxSpeed;

    private String brand;

    private double price;

    public Car() {
    }

    public Car(int maxSpeed, String brand, double price) {
        this.maxSpeed = maxSpeed;
        this.brand = brand;
        this.price = price;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return this.brand + "," + this.maxSpeed + "," + this.price;
    }
}