package com.microwu.builder;

/**
 * Description:     builder 模式:
 *      1. Student 的私有构造函数参数为Builder, 将Builder的属性赋值给 Student
 *      2. 静态内部类 Builder, 属性和Student 相同
 *      3. build 方法调用Student 的构造方法生成对象
 * @Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/16   16:47
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Student {
    private String name;
    private String sex;
    private int weight;
    private int height;
    private int age;

    private Student(Builder builder) {
        this.name = builder.name;
        this.sex = builder.sex;
        this.weight = builder.weight;
        this.height = builder.height;
        this.age = builder.age;
    }

    public static class Builder {
        private String name;
        private String sex;
        private int weight;
        private int height;
        private int age;

        public Builder(String name, String sex) {
            this.name = name;
            this.sex = sex;
        }

        public Builder setWeight(int weight) {
            this.weight = weight;
            return  this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public Student build() {
            return new Student(this);
        }

    }

    public static void main(String[] args) {
        Student build = new Builder("cxd", "man").setWeight(100).setHeight(170).setAge(24).build();
        System.out.println(build);
    }
}