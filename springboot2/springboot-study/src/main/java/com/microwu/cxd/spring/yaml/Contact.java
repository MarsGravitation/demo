package com.microwu.cxd.spring.yaml;

/**
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/9/22  13:59
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class Contact {

    private String type;
    private int number;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "type='" + type + '\'' +
                ", number=" + number +
                '}';
    }
}
