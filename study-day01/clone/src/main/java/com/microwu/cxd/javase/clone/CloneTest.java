package com.microwu.cxd.javase.clone;

import com.microwu.cxd.javase.clone.pojo.Person;
import org.junit.jupiter.api.Test;

public class CloneTest {
    /**
     *
     */
    @Test
    public void test01() throws Exception {
        Person p = new Person(23, "zhang");
        Person p1 = (Person) p.clone();

        System.out.println(p);
        System.out.println(p1);
    }

}
