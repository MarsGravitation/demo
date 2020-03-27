package com.microwu.algorithm.recursive;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 青蛙跳台阶
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/2/21   20:55
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class RecursiveDemo02 {

    /**
     * 1. 考虑特殊情况, 0 = 0, 1 = 1, 2 = 2
     * 2. 当 n = 3, 第一步可能是一步, 也可能是两步, 如果是 1步
     *      则剩下2 阶台阶, 等于走两步台阶的步数 = f(2),
     *      如果第一步是2, 则剩下 1步 = f(1)
     *
     *      所以 f(3) = f(2) + f(1)
     *
     * 存在的问题: https://mp.weixin.qq.com/s?__biz=MzI5MTU1MzM3MQ==&mid=2247483813
     * &idx=1&sn=423c8804cd708b8892763a41cfcc8886&scene=21#wechat_redirect
     *
     *
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/2/21  20:58
     *
     * @param   	n
     * @return  int
     */
    public static long recursive(int n) {
        if (n < 1) {
            throw new RuntimeException("不支持 ~~~");
        }else if (n == 1) {
            return 1;
        } else if( n == 2) {
            return 2;
        } else {
            return recursive(n - 2) + recursive(n - 1);
        }
    }

    private static Map<Integer, Long> map = new HashMap<>(64);

    public static long f(int n) {
        if(n == 1) {
            return 1;
        } else if( n == 2) {
            return 2;
        } else if ( map.containsKey(n) ) {
            return map.get(n);
        } else {
            long i = f(n - 1) + f(n - 2);
            map.put(n, i);
            return i;
        }
    }

    /**
     * 自下而上的解决办法
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/2/21  21:45
     *
     * @param   	n
     * @return  long
     */
    private static long f3(int n) {
        if(n == 1) {
            return 1;
        } else if( n == 2) {
            return 2;
        }
        long result = 0L;
        long next1 = 2L;
        long next2 = 1L;
        for(int c = 0; c <= n - 3; c++) {
            result = next1 + next2;
            next2 = next1;
            next1 = result;
        }
        return result;
    }

    public static void main(String[] args) {
        Instant startTime1 = Instant.now();
        System.out.println(recursive(55));
        Instant endTime1 = Instant.now();
        System.out.println(endTime1.compareTo(startTime1));

        Instant startTime2 = Instant.now();
        System.out.println(f(55));
        Instant endTime2 = Instant.now();
        System.out.println(endTime2.compareTo(startTime2));

        Instant startTime3 = Instant.now();
        System.out.println(f3(55));
        Instant endTime3 = Instant.now();
        System.out.println(endTime3.compareTo(startTime3));
    }

}