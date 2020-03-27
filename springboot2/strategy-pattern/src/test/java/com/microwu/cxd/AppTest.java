package com.microwu.cxd;

import com.microwu.cxd.context.SimpleContext;
import com.microwu.cxd.strategy.Strategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for simple App.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AppTest 
{

    @Autowired
    private SimpleContext simpleContext;

    @Test
    public void test() {
        Strategy resourceB = simpleContext.getResource("resourceB");
        resourceB.getStrategy("B");
    }
}
