package com.microwu.concurrent.base;

/**
 * Description: volatile
 *
 * 1. 缓存一致性协议
 * 2. 缓存一致性协议优化存在的问题
 * 3. 内存屏障
 * 4. volatile 的内存屏障
 *  a. LoadLoad 屏障
 *      第一段读指令
 *      LoadLoad
 *      第二段读指令
 *  LoadLoad：在第二段读指令被访问前，保证第一段读指令执行完毕
 *  b. StoreStore 屏障
 *      第一段写指令
 *      StoreStore
 *      第二段写指令
 *  在第二段写指令被访问前，保证第一段写指令执行完毕
 *  c. LoadStore
 *      第一段读指令
 *      LoadStore
 *      第二段写指令
 *  第二段写指令被访问前，保证第一段读指令执行完毕
 *  d. StoreLoad
 *      第一段写指令
 *      StoreLoad
 *      第二段读指令
 *  第二段读指令被访问前，保证第一段写指令执行完毕
 *
 *  volatile 的内存屏障
 *      - StoreStore -> 写操作 -> StoreLoad
 *      - LoadLoad -> 读操作 -> LoadStore
 *  保证了 volatile 具有可见性和禁止指令重排序
 *
 * 5. 总结
 *  a. 可见性：当多个线程访问同一个变量时，一个线程修改了这个变量的值，其他线程能够立即看到这个修改的值
 *  b. 有序性：对一个 volatile 变量的写操作，执行在任意后续对这个 volatile 变量的读操作之前
 *
 * https://mp.weixin.qq.com/s?__biz=MzUxODAzNDg4NQ==&mid=2247486859&idx=1&sn=a09919f9d1877b4188664294ef4694d7&chksm=f98e4921cef9c037bb642130c6265c0a07b5f964704c5fb75912f3cc78cc5ec366550fe0796d&scene=126&sessionid=1606179966&key=ef2ad97fe7aad5ec5a0835425a940a5af5c2bc9a6410330f85ae35979555e8711ea6621437332f1ac0bf75bc7fe765af374144a204ef5b7efd752f4b9bb65e2f52e066192dabdc7b5a954281126abb89e3f1124f835d2e4d12ef30f74e176d3b6adaabf15b5efd22a0d68440e05ab3cfa7de94d0ef5e2aea56265c2f2f42e8c2&ascene=1&uin=MTAwMTU2MDQyOA%3D%3D&devicetype=Windows+10+x64&version=63000039&lang=zh_CN&exportkey=A15HXya1yEpT3a%2Belg7MfUM%3D&pass_ticket=4Z2IzgqNApcU1UxW%2FAHfGsc9sbIHENFYveuRi%2BViu65sit6ksal17GC83H3lfW7o&wx_header=0
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/24   11:07
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class VolatileTest {
}