package com.microwu.cxd.spring.yaml;

/**
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/9/22  13:59
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class Address {

    private String line;
    private String city;
    private String state;
    private String zip;

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Override
    public String toString() {
        return "Address{" +
                "line='" + line + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }
}
