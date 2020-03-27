package com.microwu.enumx;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/5   16:41
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class EnumDemo {
    public static void main(String[] args) {
        Day day = Day.MONDAY;
        System.out.println(day);
    }

}

enum Day {
    MONDAY(1, "a");

    private String name;

    private int a;

    private Day(int a, String name) {
        this.a = a;
        this.name = name;
    }

    public int getA() {
        return a;
    }

    public String getName() {
        return getName();
    }
}