package com.microwu.cxd.study.bean;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import lombok.Data;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/19   16:04
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public class Person implements Comparable<Person> {

    private String firstName;

    private String lastName;

    private int zipCode;

    @Override
    public int compareTo(Person other) {
//        int cmp = lastName.compareTo(other.lastName);
//        if (cmp != 0) {
//            return cmp;
//        }
//        cmp = firstName.compareTo(other.firstName);
//        if (cmp != 0) {
//            return cmp;
//        }
//        return Integer.compare(zipCode, other.zipCode);
        return ComparisonChain.start()
                .compare(this.firstName, other.firstName)
                .compare(this.lastName, other.lastName)
                .compare(this.zipCode, other.zipCode, Ordering.natural().nullsLast())
                .result();
    }
}