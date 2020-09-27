package com.microwu.cxd.network.netty.blog.rpc.client;

import java.io.Serializable;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   15:50
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class StudentBean implements Serializable {
    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private int age;
    /**
     * 性别
     */
    private String sex;
    /**
     * 尺寸
     */
    private int size;

    public StudentBean() {

    }

    public StudentBean(String name, int age, String sex, int size) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}