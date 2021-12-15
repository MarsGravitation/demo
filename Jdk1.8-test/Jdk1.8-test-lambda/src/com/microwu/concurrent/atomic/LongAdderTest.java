package com.microwu.concurrent.atomic;

import java.util.concurrent.atomic.LongAdder;

/**
 *
 * https://mp.weixin.qq.com/s/Hbz1k5bDhdfPSb05PAZNrw
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/8/19  15:12
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class LongAdderTest {

    public static void main(String[] args) {
        LongAdder longAdder = new LongAdder();
        longAdder.increment();
    }

}
